package com.project.futabuslines.enums;

import lombok.Getter;

@Getter
public enum WatchStatus {
        SOLD_OUT("SOLD_OUT"),
        AVAILABLE("AVAILABLE");

        private final String value;

        WatchStatus(String value) {
                this.value = value;
        }
}
