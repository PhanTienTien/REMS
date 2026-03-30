package com.rems.common.constant;

public enum ActivityLogAction {

    CREATE_PROPERTY("CREATE_PROPERTY", "PROPERTY", "Tạo bất động sản"),
    APPROVE_PROPERTY("APPROVE_PROPERTY", "PROPERTY", "Duyệt bất động sản"),
    DEACTIVATE_PROPERTY("DEACTIVATE_PROPERTY", "PROPERTY", "Ẩn bất động sản"),
    RESTORE_PROPERTY("RESTORE_PROPERTY", "PROPERTY", "Khôi phục bất động sản"),
    DELETE_PROPERTY("DELETE_PROPERTY", "PROPERTY", "Xóa mềm bất động sản"),

    CREATE_BOOKING("CREATE_BOOKING", "BOOKING", "Tạo lịch hẹn cho bất động sản #%s"),
    ACCEPT_BOOKING("ACCEPT_BOOKING", "BOOKING", "Chấp nhận lịch hẹn cho bất động sản #%s"),
    REJECT_BOOKING("REJECT_BOOKING", "BOOKING", "Từ chối lịch hẹn #%s"),
    CANCEL_BOOKING("CANCEL_BOOKING", "BOOKING", "Hủy lịch hẹn #%s"),

    CREATE_TRANSACTION("CREATE_TRANSACTION", "TRANSACTION", "Tạo giao dịch cho lịch hẹn #%s"),
    COMPLETE_TRANSACTION("COMPLETE_TRANSACTION", "TRANSACTION", "Hoàn tất giao dịch cho bất động sản #%s");

    private final String action;
    private final String entityType;
    private final String descriptionTemplate;

    ActivityLogAction(String action,
                      String entityType,
                      String descriptionTemplate) {
        this.action = action;
        this.entityType = entityType;
        this.descriptionTemplate = descriptionTemplate;
    }

    public String getAction() {
        return action;
    }

    public String getEntityType() {
        return entityType;
    }

    public String buildDescription(Object... args) {
        return args == null || args.length == 0
                ? descriptionTemplate
                : String.format(descriptionTemplate, args);
    }
}
