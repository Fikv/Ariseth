package fikv.ariseth.records;

import javax.validation.constraints.NotNull;

public record UserRequestDTO(@NotNull String login,
                             @NotNull String password,
                             @NotNull String email) {}
