package org.hust.service.loader;

import junit.framework.TestCase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class LoaderServiceTest extends TestCase {
    static LoaderService loaderService;

    @BeforeAll
    static void init() {
        loaderService = new LoaderService();
    }

    @AfterAll
    static void close() {
        loaderService.close();
    }

    @Test
    void pushMappingUserId() throws IOException {
        int user_id = 11;
        String domain_userid = "abc-58";
        loaderService.pushMappingUserId(user_id, domain_userid);

        System.out.println(loaderService.getMappingUserId(domain_userid));
    }
}