package org.hust.service.loader;

import junit.framework.TestCase;
import org.hust.service.hbase.DomainUserIdList;
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

    @Test
    void getMappingUserId() throws IOException {
        String domain_userid = "c14f736e-2fa4-45d3-8d66-baf8309a58d5";
        System.out.println(loaderService.getMappingUserId(domain_userid));
    }

    @Test
    void getMappingDomainUserId() throws IOException {
        int user_id = 1245;
        DomainUserIdList domainUserIdList = loaderService.getMappingDomainUserId(user_id);
        for (String domain_userid : domainUserIdList.map.keySet()) {
            System.out.println(domain_userid);
        }
    }
}