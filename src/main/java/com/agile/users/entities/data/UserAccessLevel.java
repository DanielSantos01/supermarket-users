package com.agile.users.entities.data;

public enum UserAccessLevel {
  CASHIER(0),
  ADMIN(1);

  private int code;

  private UserAccessLevel(int code) {
    this.code = code;
  }

  public int getCode() {
    return this.code;
  }

  public static UserAccessLevel valueOf(int code) {
    for (UserAccessLevel level : UserAccessLevel.values()) {
      if (level.getCode() == code) return level;
    }

    throw new IllegalArgumentException("Invalid code");
  }
}
