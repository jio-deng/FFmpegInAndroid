package com.dzm.ffmpeg.optimize;

import com.dzm.ffmpeg.utils.ThreadUtils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description 优化启动
 * @date 2020/6/25 17:50
 */
public class LaunchOptimizeManager {
    private static final String TASK_LEAKCANARY = "task_leakcanary";
    private static final String TASK_BMOB = "task_bmob";
    private static final String TASK_BUGLY = "task_bugly";
    private static final String TASK_ANDFIX = "task_andfix";

    private static String[] allTasks = new String[] {
            TASK_LEAKCANARY, TASK_BMOB, TASK_BUGLY, TASK_ANDFIX};

    /**
     * 拓扑排序执行启动项
     *
     * @param tasks tasks
     */
    public static void topologicalSort(List<LaunchTask> tasks) {
        ExecutorService executorService = ThreadUtils.getCachedThreadPool();
        Set<String> set = new HashSet<>();
        LinkedList<LaunchTask> queue = new LinkedList<>();

        // 任务执行后，刷新排序图
        Callback callback = name -> {
            synchronized (tasks) {
                for (LaunchTask task : tasks) {
                    if (task.pres.size() > 0) {
                        task.pres.remove(name);
                        if (task.pres.size() == 0) {
                            queue.add(task);
                            tasks.remove(task);
                        }
                    }
                }
            }
        };

        for (LaunchTask task : tasks) {
            set.add(task.name);
            task.setCallback(callback);
        }

        // 排除未执行的Task
        if (set.size() < allTasks.length) {
            for (String sT : allTasks) {
                if (!set.contains(sT)) {
                    for (LaunchTask task : tasks) {
                        task.pres.remove(sT);
                    }
                }
            }
        }

        // TODO 环

        // 将可以执行的任务加入队列中
        for (int i = tasks.size() - 1; i >= 0; i --) {
            LaunchTask task = tasks.get(i);
            if (task.pres.isEmpty()) {
                queue.add(task);
                tasks.remove(task);
            }
        }

        while (!queue.isEmpty() || !tasks.isEmpty()) {
            if (queue.isEmpty()) {
                continue;
            }

            LaunchTask task = queue.poll();
            executorService.submit(task.runnable);
        }
    }

    public static class LaunchTask {
        private Runnable runnable;
        private List<String> pres;
        private String name;

        public LaunchTask(Runnable runnable, List<String> pres, String name) {
            this.runnable = runnable;
            this.pres = pres;
            this.name = name;
        }

        public void setCallback(Callback callback) {
            runnable = () -> {
                runnable.run();
                callback.onComplete(name);
            };
        }

    }

    public interface Callback {
        void onComplete(String name);
    }
}