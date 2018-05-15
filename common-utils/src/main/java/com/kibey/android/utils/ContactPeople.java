package com.kibey.android.utils;

/**
 * @author Laughing laughing.hu.android@gmail.com
 * @ClassName: ContactPeople
 * @Description: TODO 通讯录
 * @date 2013-3-18 下午1:42:34
 */
public class ContactPeople {
    /**
     * 人员名称
     **/
    private String nickname;
    private String localname;
    /**
     * 用户头像
     **/
    private String headimage;
    /**
     * 电话
     **/
    private String account;
    private String contactId;
    private String applyvalue;
    private String is_teacher;
    private String is_student;

    private String user;

    public ContactPeople() {
        super();

    }

    public String getLocal_name() {
        return localname;
    }

    public void setLocal_name(String local_name) {
        this.localname = local_name;
    }

    public String getUid() {
        return user;
    }

    public void setUid(String uid) {
        this.user = uid;
    }

    public String getName() {
        return nickname;
    }

    public void setName(String name) {
        this.nickname = name;
    }

    public String getHeadImage() {
        return headimage;
    }

    public void setHeadImage(String headImage) {
        this.headimage = headImage;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String phoneNum) {
        this.account = phoneNum;
    }

    public String getApplyvalue() {
        return AccountType.getAccountType(applyvalue).getValue() + "";
    }

    public void setApplyvalue(String applyvalue) {
        this.applyvalue = applyvalue;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public AccountType getAccountType() {
        return AccountType.getAccountType(applyvalue);
    }

    public String getIs_teacher() {
        return is_teacher;
    }

    public void setIs_teacher(String is_teacher) {
        this.is_teacher = is_teacher;
    }

    public String getIs_student() {
        return is_student;
    }

    public void setIs_student(String is_student) {
        this.is_student = is_student;
    }

    @Override
    public String toString() {
        return "ContactPeople [nickname=" + nickname + ", localname="
                + localname + ", headimage=" + headimage + ", account="
                + account + ", contactId=" + contactId + ", applyvalue="
                + applyvalue + ", is_teacher=" + is_teacher + ", is_student="
                + is_student + ", user=" + user + "]";
    }

    public enum AccountType {
        notfriend(0), notregister(-1), isfriend(1), isapply(2);
        int value;

        AccountType(int type) {
            value = type;
        }

        public int getValue() {
            return value;
        }

        public static AccountType getAccountType(int key) {
            AccountType type = null;
            switch (key) {
                case 0:
                    type = notfriend;
                    break;
                case 1:
                    type = isfriend;
                    break;
                case 2:
                    type = isapply;
                    break;
                default:
                    type = notregister;
                    break;
            }
            return type;
        }

        public static AccountType getAccountType(String key) {
            try {
                return getAccountType(Integer.valueOf(key));
            } catch (Exception e) {
                return AccountType.notregister;
            }
        }
    }

}
