package org.hust.job;

import org.hust.job.impl.batch.CollectEventBatch;
import org.hust.job.impl.stream.CollectEventStream;

public interface IJobBuilder {
    static IJobBuilder createJob(EnumJob enumJob) {
        switch (enumJob) {
            case Collect:
                return new CollectEventStream();
            case Batch:
                return new CollectEventBatch();
        }

        return null;
    }

    void run(ArgsOptional args);
}
