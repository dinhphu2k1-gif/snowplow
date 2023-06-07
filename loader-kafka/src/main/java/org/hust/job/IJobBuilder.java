package org.hust.job;

public interface IJobBuilder {
    static IJobBuilder createJob(EnumJob enumJob) {
        switch (enumJob) {
            case Collect:
                return  null;
        }

        return null;
    }

    void run(ArgsOptional args);
}
