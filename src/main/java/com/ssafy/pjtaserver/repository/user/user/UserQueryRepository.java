package com.ssafy.pjtaserver.repository.user.user;

import com.ssafy.pjtaserver.domain.user.User;

public interface UserQueryRepository {
    User getWithRoles(String userLoginId);
}
