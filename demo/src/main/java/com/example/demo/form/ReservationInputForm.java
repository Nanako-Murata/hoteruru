package com.example.demo.form;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ReservationInputForm {

    @NotBlank(message = "チェックイン日とチェックアウト日を選択してください。")
    private String fromCheckinDateToCheckoutDate;

    @NotNull(message = "宿泊人数を入力してください。")
    @Min(value = 1, message = "宿泊人数は1人以上に設定してください。")
    private Integer numberOfPeople;

    // チェックイン日を取得する
    public LocalDate getCheckinDate() {
        String[] dates = splitDatesSafely();
        return dates != null ? LocalDate.parse(dates[0]) : null;
    }

    // チェックアウト日を取得する
    public LocalDate getCheckoutDate() {
        String[] dates = splitDatesSafely();
        return dates != null ? LocalDate.parse(dates[1]) : null;
    }

    /**
     * "YYYY-MM-DD から YYYY-MM-DD" を安全に分割する
     * フォーマットが不正なら null を返す
     */
    private String[] splitDatesSafely() {
        if (fromCheckinDateToCheckoutDate == null || fromCheckinDateToCheckoutDate.isBlank()) {
            return null;
        }

        // 期待フォーマットで split
        String[] parts = fromCheckinDateToCheckoutDate.split(" から ");

        // "チェックイン日 から チェックアウト日" の形式でなければ無効
        if (parts.length != 2) {
            return null;
        }

        // 日付として parse できるかチェック
        try {
            LocalDate.parse(parts[0]);
            LocalDate.parse(parts[1]);
        } catch (DateTimeParseException e) {
            return null;
        }

        return parts;
    }

}
