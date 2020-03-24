package com.prc391.patra.members.requests;

import lombok.Data;

@Data
public class CreateMemberRequest {
    private String orgId;
    private String username;
    private Long[] permissions;
    private String[] assignedTaskId;
}