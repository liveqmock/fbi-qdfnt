package fnt.repository.dao;

import fnt.repository.model.FsQdfPaymentItem;
import fnt.repository.model.FsQdfPaymentItemExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FsQdfPaymentItemMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_QDF_PAYMENT_ITEM
     *
     * @mbggenerated Mon Jun 24 11:41:37 CST 2013
     */
    int countByExample(FsQdfPaymentItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_QDF_PAYMENT_ITEM
     *
     * @mbggenerated Mon Jun 24 11:41:37 CST 2013
     */
    int deleteByExample(FsQdfPaymentItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_QDF_PAYMENT_ITEM
     *
     * @mbggenerated Mon Jun 24 11:41:37 CST 2013
     */
    int deleteByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_QDF_PAYMENT_ITEM
     *
     * @mbggenerated Mon Jun 24 11:41:37 CST 2013
     */
    int insert(FsQdfPaymentItem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_QDF_PAYMENT_ITEM
     *
     * @mbggenerated Mon Jun 24 11:41:37 CST 2013
     */
    int insertSelective(FsQdfPaymentItem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_QDF_PAYMENT_ITEM
     *
     * @mbggenerated Mon Jun 24 11:41:37 CST 2013
     */
    List<FsQdfPaymentItem> selectByExample(FsQdfPaymentItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_QDF_PAYMENT_ITEM
     *
     * @mbggenerated Mon Jun 24 11:41:37 CST 2013
     */
    FsQdfPaymentItem selectByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_QDF_PAYMENT_ITEM
     *
     * @mbggenerated Mon Jun 24 11:41:37 CST 2013
     */
    int updateByExampleSelective(@Param("record") FsQdfPaymentItem record, @Param("example") FsQdfPaymentItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_QDF_PAYMENT_ITEM
     *
     * @mbggenerated Mon Jun 24 11:41:37 CST 2013
     */
    int updateByExample(@Param("record") FsQdfPaymentItem record, @Param("example") FsQdfPaymentItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_QDF_PAYMENT_ITEM
     *
     * @mbggenerated Mon Jun 24 11:41:37 CST 2013
     */
    int updateByPrimaryKeySelective(FsQdfPaymentItem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_QDF_PAYMENT_ITEM
     *
     * @mbggenerated Mon Jun 24 11:41:37 CST 2013
     */
    int updateByPrimaryKey(FsQdfPaymentItem record);
}