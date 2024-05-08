package com.example.demo.controller.util;

import static org.junit.Assert.assertEquals;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.controller.Role;
import com.example.demo.controller.domain.User;
import org.junit.Test;


public class CypherUtilsTest {

    @Test
    public void testCypher() {
        String originInfo = JSONObject.toJSONString(new User(123L, "bbb", Role.user));
        String encryption = CypherUtils.base64Encryption(originInfo);
        System.out.println(encryption);
        assertEquals("Base64加解密测试失败", originInfo, CypherUtils.base64Decryption(encryption));
    }
}

