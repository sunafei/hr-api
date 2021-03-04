package com.eplugger.module.business;

import com.eplugger.module.base.BizEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Component
@Table(name = "biz_interview_register")
@Data
@NoArgsConstructor
public class InterviewRegister extends BizEntity {
    // 基本信息
    private String name;
    private String sex;
    private String position; // 应聘职位
    private String interviewer; // 面试官
    private String phone;
    private String email;
    private String birthday;
    private String place; // 籍贯
    private String nation; // 民族
    private String marryStatus; // 婚姻状态
    private String address;
    private String socialSecurity; // 社保状态
    private String currentSalary; // 当前薪资
    private String expectedSalary; // 期望薪资

    // 教育经历
    private String schoolName1;
    private String education1;// 学历
    private String timeSolt1;// 时间段
    private String major1;// 专业
    private String schoolName2;
    private String education2;
    private String timeSolt2;
    private String major2;
    private String schoolName3;
    private String education3;
    private String timeSolt3;
    private String major3;

    // 工作履历
    private String startDate1;
    private String endDate1;
    private String company1;
    private String post1;
    private String superior1;
    private String tel1;
    private String startDate2;
    private String endDate2;
    private String company2;
    private String post2;
    private String superior2;
    private String tel2;
    private String startDate3;
    private String endDate3;
    private String company3;
    private String post3;
    private String superior3;
    private String tel3;

    // 面试题附件
	private String fileIds;
	// 面试评价
	private String evaluate;

	private String result;
}
