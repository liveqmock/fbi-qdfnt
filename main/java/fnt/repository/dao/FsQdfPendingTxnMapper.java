package fnt.repository.dao;

import fnt.repository.model.FsQdfPendingTxn;
import fnt.repository.model.FsQdfPendingTxnExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FsQdfPendingTxnMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_QDF_PENDING_TXN
     *
     * @mbggenerated Mon Jul 08 20:57:26 CST 2013
     */
    int countByExample(FsQdfPendingTxnExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_QDF_PENDING_TXN
     *
     * @mbggenerated Mon Jul 08 20:57:26 CST 2013
     */
    int deleteByExample(FsQdfPendingTxnExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_QDF_PENDING_TXN
     *
     * @mbggenerated Mon Jul 08 20:57:26 CST 2013
     */
    int deleteByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_QDF_PENDING_TXN
     *
     * @mbggenerated Mon Jul 08 20:57:26 CST 2013
     */
    int insert(FsQdfPendingTxn record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_QDF_PENDING_TXN
     *
     * @mbggenerated Mon Jul 08 20:57:26 CST 2013
     */
    int insertSelective(FsQdfPendingTxn record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_QDF_PENDING_TXN
     *
     * @mbggenerated Mon Jul 08 20:57:26 CST 2013
     */
    List<FsQdfPendingTxn> selectByExample(FsQdfPendingTxnExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_QDF_PENDING_TXN
     *
     * @mbggenerated Mon Jul 08 20:57:26 CST 2013
     */
    FsQdfPendingTxn selectByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_QDF_PENDING_TXN
     *
     * @mbggenerated Mon Jul 08 20:57:26 CST 2013
     */
    int updateByExampleSelective(@Param("record") FsQdfPendingTxn record, @Param("example") FsQdfPendingTxnExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_QDF_PENDING_TXN
     *
     * @mbggenerated Mon Jul 08 20:57:26 CST 2013
     */
    int updateByExample(@Param("record") FsQdfPendingTxn record, @Param("example") FsQdfPendingTxnExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_QDF_PENDING_TXN
     *
     * @mbggenerated Mon Jul 08 20:57:26 CST 2013
     */
    int updateByPrimaryKeySelective(FsQdfPendingTxn record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_QDF_PENDING_TXN
     *
     * @mbggenerated Mon Jul 08 20:57:26 CST 2013
     */
    int updateByPrimaryKey(FsQdfPendingTxn record);
}