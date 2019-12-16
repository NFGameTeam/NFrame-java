package com.noahframe.service.mall.service;


import com.google.gson.Gson;
import com.noahframe.nfweb.NFIBaseService;
import com.noahframe.plugins.logic.mall.database.front.Member;
import com.noahframe.plugins.logic.mall.database.mapper.TbMemberMapper;
import com.noahframe.plugins.logic.mall.database.model.TbMember;
import com.noahframe.plugins.logic.mall.database.pojo.MemberDto;

import com.noahframe.plugins.logic.mall.database.iface.LoginService;
import com.noahframe.plugins.logic.mall.database.iface.MemberService;
import com.noahframe.plugins.logic.mall.datas.DataTablesResult;
import com.noahframe.plugins.logic.mall.datas.exception.NFmallException;
import com.noahframe.plugins.logic.mall.datas.utils.QiniuUtil;
import org.springframework.stereotype.Service;


/**
 *
 * @author Exrick
 * @date 2017/8/11
 */
@Service
public class MemberServiceImpl extends NFIBaseService implements MemberService {


    @Override
    public TbMember getMemberById(long memberId) {

        MemberService m_pMemberService=pPluginManager.FindModule(MemberService.class);

        return m_pMemberService.getMemberById(memberId);
    }

    @Override
    public DataTablesResult getMemberList(int draw, int start, int length, String search, String minDate, String maxDate, String orderCol, String orderDir) {
        MemberService m_pMemberService=pPluginManager.FindModule(MemberService.class);
        return m_pMemberService.getMemberList(draw, start, length, search, minDate, maxDate, orderCol, orderDir);
    }

    @Override
    public DataTablesResult getRemoveMemberList(int draw, int start, int length, String search, String minDate, String maxDate, String orderCol, String orderDir) {
        MemberService m_pMemberService=pPluginManager.FindModule(MemberService.class);
        return m_pMemberService.getRemoveMemberList(draw, start, length, search, minDate, maxDate, orderCol, orderDir);
    }

    @Override
    public DataTablesResult getMemberCount() {
        MemberService m_pMemberService=pPluginManager.FindModule(MemberService.class);
        return m_pMemberService.getMemberCount();
    }

    @Override
    public DataTablesResult getRemoveMemberCount() {
        MemberService m_pMemberService=pPluginManager.FindModule(MemberService.class);
        return m_pMemberService.getRemoveMemberCount();
    }

    @Override
    public TbMember getMemberByEmail(String email) {
        MemberService m_pMemberService=pPluginManager.FindModule(MemberService.class);
        return m_pMemberService.getMemberByEmail(email);
    }

    @Override
    public TbMember getMemberByPhone(String phone) {
        MemberService m_pMemberService=pPluginManager.FindModule(MemberService.class);
        return m_pMemberService.getMemberByPhone(phone);
    }

    @Override
    public TbMember getMemberByUsername(String username) {
        MemberService m_pMemberService=pPluginManager.FindModule(MemberService.class);
        return m_pMemberService.getMemberByUsername(username);
    }

    @Override
    public TbMember addMember(MemberDto memberDto) {
        MemberService m_pMemberService=pPluginManager.FindModule(MemberService.class);
        return m_pMemberService.addMember(memberDto);
    }

    @Override
    public TbMember updateMember(Long id, MemberDto memberDto) {
        MemberService m_pMemberService=pPluginManager.FindModule(MemberService.class);
        return m_pMemberService.updateMember(id, memberDto);
    }

    @Override
    public TbMember changePassMember(Long id, MemberDto memberDto) {
        MemberService m_pMemberService=pPluginManager.FindModule(MemberService.class);
        return m_pMemberService.changePassMember(id, memberDto);
    }

    @Override
    public TbMember alertMemberState(Long id, Integer state) {
        MemberService m_pMemberService=pPluginManager.FindModule(MemberService.class);
        return m_pMemberService.alertMemberState(id, state);
    }

    @Override
    public int deleteMember(Long id) {
        MemberService m_pMemberService=pPluginManager.FindModule(MemberService.class);
        return m_pMemberService.deleteMember(id);
    }

    @Override
    public TbMember getMemberByEditEmail(Long id, String email) {
        MemberService m_pMemberService=pPluginManager.FindModule(MemberService.class);
        return m_pMemberService.getMemberByEditEmail(id, email);
    }

    @Override
    public TbMember getMemberByEditPhone(Long id, String phone) {
        MemberService m_pMemberService=pPluginManager.FindModule(MemberService.class);
        return m_pMemberService.getMemberByEditPhone(id, phone);
    }

    @Override
    public TbMember getMemberByEditUsername(Long id, String username) {
        MemberService m_pMemberService=pPluginManager.FindModule(MemberService.class);
        return m_pMemberService.getMemberByEditUsername(id, username);
    }

    @Override
    public String imageUpload(Long userId,String token,String imgData) {
        MemberService m_pMemberService = pPluginManager.FindModule(MemberService.class);
        return m_pMemberService.imageUpload(userId, token, imgData);
    }
}
