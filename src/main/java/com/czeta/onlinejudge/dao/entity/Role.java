package com.czeta.onlinejudge.dao.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Role {
    private Long id;
    private String name;
    private String permissionCodes;
    private Integer status;
    private String crtTs;
    private String lmTs;
}
