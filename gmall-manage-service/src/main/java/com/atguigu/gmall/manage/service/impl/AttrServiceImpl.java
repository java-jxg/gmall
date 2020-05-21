package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsBaseAttrValue;
import com.atguigu.gmall.manage.mapper.PmsBaseAttrInfoMapper;
import com.atguigu.gmall.manage.mapper.PmsBaseAttrValueMapper;
import com.atguigu.gmall.service.AttrService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class AttrServiceImpl implements AttrService {

    @Autowired
    PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;
    @Autowired
    PmsBaseAttrValueMapper pmsBaseAttrValueMapper;

    @Override
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id) {
        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(catalog3Id);
        List<PmsBaseAttrInfo> select = pmsBaseAttrInfoMapper.select(pmsBaseAttrInfo);
        return select;
    }

    @Override
    public void saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {
        if(StringUtils.isBlank(pmsBaseAttrInfo.getId())){
            //添加
            pmsBaseAttrInfoMapper.insertSelective(pmsBaseAttrInfo);
            List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
            for(PmsBaseAttrValue p : attrValueList){
                p.setAttrId(pmsBaseAttrInfo.getId());
                pmsBaseAttrValueMapper.insert(p);
            }
        }{
            //修改
            pmsBaseAttrInfoMapper.updateByPrimaryKeySelective(pmsBaseAttrInfo);
            //先删除
            PmsBaseAttrValue pa = new PmsBaseAttrValue();
            pa.setAttrId(pmsBaseAttrInfo.getId());
            pmsBaseAttrValueMapper.delete(pa);
            //后添加
            List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
            for(PmsBaseAttrValue p : attrValueList){
                p.setAttrId(pmsBaseAttrInfo.getId());
                pmsBaseAttrValueMapper.insert(p);
            }
        }

    }

    @Override
    public List<PmsBaseAttrValue> getAttrValueList(String attrId) {
        PmsBaseAttrValue p = new PmsBaseAttrValue();
        p.setAttrId(attrId);
        List<PmsBaseAttrValue> pmsBaseAttrValues = pmsBaseAttrValueMapper.select(p);
        return pmsBaseAttrValues;
    }
}
