package fnt.service;

import fnt.repository.dao.FsQdfPaymentInfoMapper;
import fnt.repository.model.FsQdfPaymentInfo;
import fnt.repository.model.FsQdfPaymentInfoExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 13-7-5
 * Time: ÏÂÎç8:30
 * To change this template use File | Settings | File Templates.
 */
@Service
public class PaymentQryService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentQryService.class);

    @Autowired
    private FsQdfPaymentInfoMapper fsQdfPaymentInfoMapper;

    public List<FsQdfPaymentInfo> qryPaymentInfos(String date8) {
        FsQdfPaymentInfoExample example = new FsQdfPaymentInfoExample();
        example.createCriteria().andChkActDtEqualTo(date8).andPendingFlagEqualTo("0").andHostBookFlagEqualTo("1");
        return fsQdfPaymentInfoMapper.selectByExample(example);
    }

    public FsQdfPaymentInfo qryBmkPaymentInfo(String bmkywxh) {
        FsQdfPaymentInfoExample example = new FsQdfPaymentInfoExample();
        example.createCriteria().andBmkywxhEqualTo(bmkywxh).andPendingFlagEqualTo("3")
                .andQdfChkFlagEqualTo("1");
        List<FsQdfPaymentInfo> infos = fsQdfPaymentInfoMapper.selectByExample(example);
        return infos.size() > 0 ? infos.get(0) : null;
    }

    public List<FsQdfPaymentInfo> qryChkedBmkPaymentInfos(String date8) {
        FsQdfPaymentInfoExample example = new FsQdfPaymentInfoExample();
        example.createCriteria().andChkActDtEqualTo(date8).andPendingFlagEqualTo("3").andQdfChkFlagEqualTo("1");
        return fsQdfPaymentInfoMapper.selectByExample(example);
    }
}
