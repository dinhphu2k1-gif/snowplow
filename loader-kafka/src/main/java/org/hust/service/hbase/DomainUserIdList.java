package org.hust.service.hbase;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class DomainUserIdList {
    private static final long serialVersionUID = 3190821288356036866L;
    public Map<String, Long> map;

    public DomainUserIdList() {
        map = new HashMap<>();
    }
}
