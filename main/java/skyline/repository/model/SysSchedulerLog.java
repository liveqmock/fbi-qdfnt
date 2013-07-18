package skyline.repository.model;

import java.util.Date;

public class SysSchedulerLog {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SYS_SCHEDULER_LOG.JOBID
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    private Integer jobid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SYS_SCHEDULER_LOG.JOBNAME
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    private String jobname;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SYS_SCHEDULER_LOG.TIME
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    private Date time;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SYS_SCHEDULER_LOG.INFO
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    private String info;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SYS_SCHEDULER_LOG.JOBID
     *
     * @return the value of SYS_SCHEDULER_LOG.JOBID
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public Integer getJobid() {
        return jobid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SYS_SCHEDULER_LOG.JOBID
     *
     * @param jobid the value for SYS_SCHEDULER_LOG.JOBID
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public void setJobid(Integer jobid) {
        this.jobid = jobid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SYS_SCHEDULER_LOG.JOBNAME
     *
     * @return the value of SYS_SCHEDULER_LOG.JOBNAME
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public String getJobname() {
        return jobname;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SYS_SCHEDULER_LOG.JOBNAME
     *
     * @param jobname the value for SYS_SCHEDULER_LOG.JOBNAME
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public void setJobname(String jobname) {
        this.jobname = jobname == null ? null : jobname.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SYS_SCHEDULER_LOG.TIME
     *
     * @return the value of SYS_SCHEDULER_LOG.TIME
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public Date getTime() {
        return time;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SYS_SCHEDULER_LOG.TIME
     *
     * @param time the value for SYS_SCHEDULER_LOG.TIME
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SYS_SCHEDULER_LOG.INFO
     *
     * @return the value of SYS_SCHEDULER_LOG.INFO
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public String getInfo() {
        return info;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SYS_SCHEDULER_LOG.INFO
     *
     * @param info the value for SYS_SCHEDULER_LOG.INFO
     *
     * @mbggenerated Fri Jul 22 13:16:43 CST 2011
     */
    public void setInfo(String info) {
        this.info = info == null ? null : info.trim();
    }
}