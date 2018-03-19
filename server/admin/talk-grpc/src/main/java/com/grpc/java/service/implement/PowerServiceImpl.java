package com.grpc.java.service.implement;


import com.grpc.java.kernel.entity.power_info;
import com.grpc.java.kernel.mybatis.mapper.power_infoMapper;
import com.grpc.java.service.PowerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/*******************************
 * Created by wx on 2017/10/27.*
 *******************************/
@Service("powerService")
public class PowerServiceImpl implements PowerService {
    @Resource
    private power_infoMapper powerMapper;
    
    @Override
    public power_info getId(Integer id) {

        return powerMapper.selectById(id);
    }
   
    @Override
    public power_info getName(String name){
        return powerMapper.selectByName(name);
    }

    @Override
    public power_info getUrl(String url){
        return powerMapper.selectByUrl(url);
    }
    

    @Override
    public List<power_info> getAll() {

        return this.powerMapper.selectAll();
    }

    @Override
    public Boolean add(power_info user) {
        return this.powerMapper.insertSelective(user) > 0;
    }

    @Override
    public Boolean update(power_info user){
        return this.powerMapper.updateByPrimaryKeySelective(user)>0;
    }

    @Override
    public Boolean delete(Integer id){
        return this.powerMapper.deleteByPrimaryKey(id) > 0;
    }


}
