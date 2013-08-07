package fnt.repository.dao.common;

import fnt.repository.model.FsQdfPendingTxn;
import fnt.repository.model.common.DayGatherData;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 */
@Component
public interface CommonMapper {

    /*@Select("select info.wtzsdwbm,info.wtzsdwmc,item.srxmbm, item.srxmmc, " +
            " item.yskmbm, item.yskmmc, info.pending_flag as pendingflag, item.zjxzbm, " +
            " item.zjxzmc, item.je as zje, sifo.hrxzqh as hrxzqh, sifo.fcje as fcje " +
            " from fs_qdf_payment_item item" +
            " left join fs_qdf_payment_info info" +
            " on info.pjzl = item.pjzl" +
            " and info.jksbh = item.jksbh" +
            " left join fs_qdf_share_info sifo" +
            " on item.pjzl = sifo.pjzl" +
            " and item.jksbh = sifo.jksbh" +
            " and substr(item.srxmbm,1,8) = sifo.hcxmbm where info.chk_act_dt = #{date8}")*/
    @Select(" select wtzsdwbm," +
            "       wtzsdwmc," +
            "       srxmbm," +
            "       srxmmc," +
            "       yskmbm," +
            "       yskmmc," +
            "       sum(je) as zje," +
            "       pendingflag," +
            "       zjxzbm," +
            "       zjxzmc," +
            "       hrxzqh," +
            "       sum(fcje) as fcje" +
            "  from (select info.wtzsdwbm," +
            "               info.wtzsdwmc," +
            "               item.srxmbm," +
            "               item.srxmmc," +
            "               item.yskmbm," +
            "               item.yskmmc," +
            "               info.pending_flag as pendingflag," +
            "               item.zjxzbm," +
            "               item.zjxzmc," +
            "               item.je," +
            "               sifo.hrxzqh," +
            "               sifo.fcje" +
            "          from fs_qdf_payment_item item" +
            "          left join fs_qdf_payment_info info" +
            "            on info.pjzl = item.pjzl" +
            "           and info.jksbh = item.jksbh" +
            "          left join fs_qdf_share_info sifo" +
            "            on item.pjzl = sifo.pjzl" +
            "           and item.jksbh = sifo.jksbh" +
            "           and substr(item.srxmbm, 1, 8) = sifo.hcxmbm" +
            "         where info.chk_act_dt = #{date8})" +
            "  group by wtzsdwbm," +
            "          wtzsdwmc," +
            "          srxmbm," +
            "          srxmmc," +
            "          yskmbm," +
            "          yskmmc," +
            "          pendingflag," +
            "          zjxzbm," +
            "          zjxzmc," +
            "          hrxzqh")
    List<DayGatherData> qryDayGatherDataByDate(@Param("date8") String date8);

    @Select(" select t.XZQH, t.BMKYWXH, CZZHZH, t.JKFS, YT, JE, JYRQ, TXAC_BRID as txacBrid, " +
            " BOOK_OPERID as bookOperid, t.OPER_TIME as operTime" +
            " from fs_qdf_pending_txn t" +
            " join fs_qdf_payment_info info" +
            " on t.bmkywxh = info.bmkywxh" +
            " and info.pending_flag = '3'" +
            " and info.qdf_chk_flag = '1'" +
            " and t.jyrq != #{date8} and info.chk_act_dt = #{date8}")
    List<FsQdfPendingTxn> qryChkedFormerPendingTxns(String date8);


}