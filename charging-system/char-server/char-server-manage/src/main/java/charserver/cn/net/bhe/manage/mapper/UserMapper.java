package charserver.cn.net.bhe.manage.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

public interface UserMapper {

    @Select({ " <script> "
            //
            + " SELECT id \"id\", phone \"phone\", name \"name\", password \"password\", sex \"sex\", age \"age\", address \"address\", type \"type\", role \"role\", status \"status\", updatetime \"updatetime\", createtime \"createtime\", ip \"ip\" FROM CS_USER WHERE type = 1 AND phone = #{phone} "
            //
            + " </script> " })
    @Results({ @Result(property = "createtime", column = "createtime", jdbcType = JdbcType.TIMESTAMP)
            //
            , @Result(property = "updatetime", column = "updatetime", jdbcType = JdbcType.TIMESTAMP) })
    public Map<String, Object> login(Map<String, Object> rq_u);

    @Update({ " <script> "
            //
            + " UPDATE CS_USER SET ip = #{ip}, updatetime = NOW() WHERE type = 1 AND phone = #{phone} "
            //
            + " </script> " })
    public int ipUpdate(@Param("phone") String phone, @Param("ip") String ip);

    @Select({ " <script> "
            //
            + " SELECT id \"id\", phone \"phone\", name \"name\", password \"password\", sex \"sex\", age \"age\", address \"address\", type \"type\", role \"role\", status \"status\", updatetime \"updatetime\", createtime \"createtime\", ip \"ip\" FROM CS_USER WHERE type = 1 AND phone = #{phone} "
            //
            + " </script> " })
    @Results({ @Result(property = "createtime", column = "createtime", jdbcType = JdbcType.TIMESTAMP)
            //
            , @Result(property = "updatetime", column = "updatetime", jdbcType = JdbcType.TIMESTAMP) })
    public Map<String, Object> userCheckoutByPhone(@Param("phone") String phone);

    @Update({ " <script> "
            //
            + " UPDATE CS_USER SET password = #{password}, updatetime = NOW() WHERE type = 1 AND phone = #{phone} "
            //
            + " </script> " })
    public int pwdReset(@Param("phone") String phone, @Param("password") String password);

    @Update({ " <script> "
            //
            + " UPDATE CS_USER SET updatetime = NOW() "
            //
            + " <if test='name!=null and name!=\"\"'>, name = #{name} </if> "
            //
            + " <if test='sex!=null'>, sex = #{sex} </if> "
            //
            + " <if test='age!=null'>, age = #{age} </if> "
            //
            + " <if test='address!=null and address!=\"\"'>, address = #{address} </if> "
            //
            + " <if test='ip!=null and ip!=\"\"'>, ip = #{ip} </if> "
            //
            + " WHERE type = 1 AND phone = #{phone} "
            //
            + " </script> " })
    public int update(Map<String, Object> rq_u);

    @Select({ " <script> "
            //
            + " SELECT u.id \"id\", u.phone \"phone\", u.name \"name\", u.sex \"sex\", u.age \"age\", u.address \"address\", u.type \"type\", u.role \"role\", u.status \"status\", u.updatetime \"updatetime\", u.createtime \"createtime\", u.ip \"ip\", nd.name \"area\" FROM CS_USER u "
            //
            + " LEFT JOIN CS_USER_REF_NODE ref ON ref.user_id = u.id "
            //
            + " LEFT JOIN CS_NODE nd ON nd.id = ref.node_id "
            //
            + " WHERE 1 = 1 "
            //
            + " <choose> "
            //
            + " <when test='type==1 and role==0'> AND ((u.type = 1 AND u.role IN (1, 2)) OR (u.type = 2)) </when> "
            //
            + " <when test='type==1 and role!=0'> AND u.type = 2 </when> "
            //
            + " <otherwise> FALSE </otherwise> "
            //
            + " </choose> "
            //
            + " <if test='nodeIds!=null and nodeIds!=\"\"'> AND ref.node_id IN (${nodeIds}) </if> "
            //
            + " <if test='phone!=null and phone!=\"\"'> AND u.phone LIKE CONCAT('%', #{phone}, '%') </if> "
            //
            + " <if test='name!=null and name!=\"\"'> AND u.name LIKE CONCAT('%', #{name}, '%') </if> "
            //
            + " ORDER BY phone, id "
            //
            + " LIMIT #{offset}, #{limit} </script> " })
    public List<Map<String, Object>> userList(Map<String, Object> rq_u);

    @Select({ " <script> SELECT COUNT(0) FROM ( "
            //
            + " SELECT u.id \"id\", u.phone \"phone\", u.name \"name\", u.sex \"sex\", u.age \"age\", u.address \"address\", u.type \"type\", u.role \"role\", u.status \"status\", u.updatetime \"updatetime\", u.createtime \"createtime\", u.ip \"ip\", nd.name \"area\" FROM CS_USER u "
            //
            + " INNER JOIN CS_USER_REF_NODE ref ON ref.user_id = u.id "
            //
            + " INNER JOIN CS_NODE nd ON nd.id = ref.node_id "
            //
            + " WHERE 1 = 1 "
            //
            + " <choose> "
            //
            + " <when test='type==1 and role==0'> AND ((u.type = 1 AND u.role IN (1, 2)) OR (u.type = 2)) </when> "
            //
            + " <when test='type==1 and role!=0'> AND u.type = 2 </when> "
            //
            + " <otherwise> FALSE </otherwise> "
            //
            + " </choose> "
            //
            + " <if test='nodeIds!=null and nodeIds!=\"\"'> AND ref.node_id IN (${nodeIds}) </if> "
            //
            + " <if test='phone!=null and phone!=\"\"'> AND u.phone LIKE CONCAT('%', #{phone}, '%') </if> "
            //
            + " <if test='name!=null and name!=\"\"'> AND u.name LIKE CONCAT('%', #{name}, '%') </if> "
            //
            + " ) c </script> " })
    public int userCount(Map<String, Object> rq_u);

    @Update({ " <script> "
            //
            + " UPDATE CS_USER SET updatetime = NOW() "
            //
            + " <if test='phone!=null and phone!=\"\"'>, phone = #{phone} </if> "
            //
            + " <if test='name!=null and name!=\"\"'>, name = #{name} </if> "
            //
            + " <if test='password!=null and password!=\"\"'>, password = #{password} </if> "
            //
            + " <if test='sex!=null'>, sex = #{sex} </if> "
            //
            + " <if test='age!=null'>, age = #{age} </if> "
            //
            + " <if test='address!=null and address!=\"\"'>, address = #{address} </if> "
            //
            + " <if test='role!=null'>, role = #{role} </if> "
            //
            + " <if test='status!=null'>, status = #{status} </if> "
            //
            + " <if test='ip!=null and ip!=\"\"'>, ip = #{ip} </if> "
            //
            + " WHERE id = #{id} "
            //
            + " </script> " })
    public int subUpdate(Map<String, Object> rq_u);

    @Select({ " <script> "
            //
            + " SELECT id \"id\" FROM CS_USER WHERE 1 = 1 "
            //
            + " <if test='id!=null'> AND id != #{id} </if> "
            //
            + " AND type = #{type} AND phone = #{phone} "
            //
            + " </script> " })
    public Map<String, Object> phoneCheck(Map<String, Object> rq_u);

    @Select({ " <script> "
            //
            + " SELECT id \"id\", phone \"phone\", name \"name\", password \"password\", sex \"sex\", age \"age\", address \"address\", type \"type\", role \"role\", status \"status\", updatetime \"updatetime\", createtime \"createtime\", ip \"ip\" FROM CS_USER WHERE id = #{id} "
            //
            + " </script> " })
    @Results({ @Result(property = "createtime", column = "createtime", jdbcType = JdbcType.TIMESTAMP)
            //
            , @Result(property = "updatetime", column = "updatetime", jdbcType = JdbcType.TIMESTAMP) })
    public Map<String, Object> userGet(@Param("id") int id);

    @Select({ " <script> "
            //
            + " SELECT id \"id\", name \"name\", p_id \"p_id\", createtime \"createtime\", updatetime \"updatetime\", remark \"remark\", status \"status\", type \"type\" FROM CS_NODE "
            //
            + " </script> " })
    public List<Map<String, Object>> nodeList();

    @Select({ " <script> "
            //
            + " SELECT hieQueDown(#{nodeId}) "
            //
            + " </script> " })
    public String subNodeIdsGet(@Param("nodeId") int nodeId);

    @Insert({ " <script> "
            //
            + " INSERT INTO CS_USER (phone, name, password, sex, age, address, type, role, status, updatetime, createtime, ip) "
            //
            + " VALUES (#{phone}, #{name}, #{password}, #{sex}, #{age}, #{address}, #{type}, #{role}, 1, NOW(), NOW(), #{ip}) "
            //
            + " </script> " })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public int subAdd(Map<String, Object> rq_u);

    @Select({ " <script> "
            //
            + " SELECT id \"id\" FROM CS_NODE WHERE id = #{id} "
            //
            + " </script> " })
    public Map<String, Object> nodeGet(@Param("id") int id);

    @Insert({ " <script> "
            //
            + " INSERT INTO CS_USER_REF_NODE (user_id, node_id, createtime) "
            //
            + " VALUES (#{userId}, #{nodeId}, NOW()) "
            //
            + " </script> " })
    public int userRefNodeAdd(@Param("userId") int userId, @Param("nodeId") int nodeId);

    @Delete({ " <script> "
            //
            + " DELETE FROM CS_USER_REF_NODE WHERE user_id = #{userId} "
            //
            + " </script> " })
    public int userRefNodeDel(@Param("userId") int userId);

    @Insert({ " <script> "
            //
            + " INSERT INTO CS_WALLET (num, user_id, credit, balance, updatetime, createtime) "
            //
            + " VALUES (#{num}, #{userId}, 0, 0, NOW(), NOW()) "
            //
            + " </script> " })
    public int walletAdd(Map<String, Object> rq_u);

    @Insert({ " <script> "
            //
            + " INSERT INTO CS_WATER (eqm_num, user_id, old_value, new_value, updatetime, createtime) "
            //
            + " VALUES (#{eqmNum}, #{userId}, 0, 0, NOW(), NOW()) "
            //
            + " </script> " })
    public int waterAdd(Map<String, Object> rq_u);
}
