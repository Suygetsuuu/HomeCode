package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.controller.domain.Result;
import com.example.demo.controller.domain.User;
import com.example.demo.controller.dto.UserResourcesDTO;
import com.example.demo.controller.exception.AuthException;
import com.example.demo.controller.exception.FileReadWriteException;
import com.example.demo.controller.file.ObjectFileMapper;
import com.example.demo.controller.util.CypherUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @RequestMapping("/admin/addUser")
    @ResponseBody
    public Result<String> addUser(@RequestHeader("Authorization") String authHeader,
                          @RequestBody UserResourcesDTO userResourcesDTO) {

        Result<String> result = new Result<>();
        try {
            User user = roleAuthentication(authHeader);
            // 角色是admin，可以addUser
            if (user.getRole() == Role.admin) {
                // 添加角色关系到文件中
                List<UserResourcesDTO> userResourcesDTOS = new ArrayList<>();
                userResourcesDTOS.add(userResourcesDTO);
                ObjectFileMapper.writeFile(userResourcesDTOS);
            } else {
                result.setCode(302);
                result.setMsg("The current user does not have the authority to add users");
                return result;
            }
        } catch (AuthException ae) {
            result.setCode(302);
            result.setMsg("Identity verification exception");
            return result;
        } catch (FileReadWriteException fe) {
            result.setCode(503);
            result.setMsg("Exception occured while writing file: " + fe.getMessage());
            return result;
        } catch (Exception e) {
            result.setCode(503);
            result.setMsg("Exception occured : " + e.getMessage());
            return result;
        }

        result.setCode(200);
        result.setData("success to add user");
        return result;
    }

    @RequestMapping("/user/{resource}")
    @ResponseBody
    public Result<String> getResources(@RequestHeader("Authorization") String authHeader,
                             @PathVariable("resource") String resource) {
        Result<String> result = new Result<>();

        try {
            User user = roleAuthentication(authHeader);
            // 角色是admin，可以获取所有资源
            if (user.getRole() == Role.admin) {
                result.setCode(200);
                result.setData("The current user " + user.getUserId() + " has permission to view this page " + resource);
            }

            // 其他角色根据userId查看是否有权限getResources
            else {
                List<String> authorizedResources = getAuthorizedResourcesByUserId(user.getUserId());
                if (authorizedResources.contains(resource)) {
                    result.setCode(200);
                    result.setData("The current user " + user.getUserId() + " has permission to view this page " + resource);
                } else {
                    result.setCode(302);
                    result.setData("The current user " + user.getUserId() + " does not have permission to view this page " + resource);
                }
            }
        } catch (AuthException ae) {
            result.setCode(302);
            result.setMsg("Identity verification exception");
            return result;
        } catch (FileReadWriteException fe) {
            result.setCode(503);
            result.setMsg("Exception occured while reading file: " + fe.getMessage());
        }
        return result;
     }

    private User roleAuthentication(String encodedInfo) throws AuthException {
        if (encodedInfo != null && encodedInfo != "") {
            User curUser = new User();
            try {
                String decodedInfo = CypherUtils.base64Decryption(encodedInfo);
                JSONObject jsonObject = JSONObject.parseObject(decodedInfo);
                curUser.setUserId(jsonObject.getLong("userId"));
                curUser.setAccountName(jsonObject.getString("accountName"));
                if ("admin".equals(jsonObject.getString("role"))) {
                    curUser.setRole(Role.admin);
                } else if ("user".equals(jsonObject.getString("role"))) {
                    curUser.setRole(Role.user);
                }
            } catch (Exception e) {
                throw new AuthException("当前用户身份验证异常");
            }

            if (curUser.getUserId() == null || curUser.getAccountName() == null || curUser.getRole() == null) {
                throw new AuthException("当前用户身份验证异常");
            }
            return curUser;
        } else {
            throw new AuthException("当前用户身份验证异常");
        }
    }

    private List<String> getAuthorizedResourcesByUserId(Long userId) throws FileReadWriteException{
        List<String> resources = new ArrayList<>();
        List<UserResourcesDTO> userResourcesDTOS = ObjectFileMapper.readFile();
        List<UserResourcesDTO> filteredUserResources = userResourcesDTOS.stream().filter(userResourcesDTO -> userId.equals(userResourcesDTO.getUserId())).collect(Collectors.toList());
        for(UserResourcesDTO userResourcesDTO : filteredUserResources) {
            if (null != userResourcesDTO.getEndpoint() && !userResourcesDTO.getEndpoint().isEmpty()) {
                resources.addAll(userResourcesDTO.getEndpoint());
            }
        }

        return resources;
    }
}
