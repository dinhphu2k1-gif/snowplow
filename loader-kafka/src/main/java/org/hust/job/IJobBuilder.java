package org.hust.job;

import org.hust.job.impl.CollectEvent;

public interface IJobBuilder {
    static IJobBuilder createJob(EnumJob enumJob) {
        switch (enumJob) {
            case Collect:
                return new CollectEvent();
        }

        return null;
    }

    void run(ArgsOptional args);
}
