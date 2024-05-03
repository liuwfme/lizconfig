package cn.liz.lizconfig.server.mapper;

import cn.liz.lizconfig.server.model.Configs;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ConfigsMapper {

    @Select("select * from configs where app=#{app} and env=#{env} and namespace=#{namespace}")
    List<Configs> list(String app, String env, String namespace);

    @Select("select * from configs where app=#{app} and env=#{env} and namespace=#{namespace} and pkey=#{pkey}")
    Configs select(String app, String env, String namespace, String pkey);

    @Insert("insert into configs (app, env, namespace, pkey, pval) values (#{app}, #{env}, #{namespace}, #{pkey}, #{pval})")
    void insert(Configs configs);

    @Update("update configs set pval=#{pval} where app=#{app} and env=#{env} and namespace=#{namespace} and pkey=#{pkey}")
    void update(Configs configs);
}
