package org.contract_lib.lang.verifast.ast;

import java.util.function.Function;

public interface VeriFastComment {

  public <R> R perform(
      Function<Inline, R> inline,
      Function<Multiline, R> multiline,
      Function<EndLine, R> endLine);

  public record Inline(String commentBody) implements VeriFastComment {

    @Override
    public <R> R perform(
        Function<Inline, R> inline,
        Function<Multiline, R> multiline,
        Function<EndLine, R> endLine) {
      return inline.apply(this);
    }
  }

  public record Multiline(String commentBody) implements VeriFastComment {
    @Override
    public <R> R perform(
        Function<Inline, R> inline,
        Function<Multiline, R> multiline,
        Function<EndLine, R> endLine) {
      return multiline.apply(this);
    }
  }

  public record EndLine(String commentBody) implements VeriFastComment {
    @Override
    public <R> R perform(
        Function<Inline, R> inline,
        Function<Multiline, R> multiline,
        Function<EndLine, R> endLine) {
      return endLine.apply(this);
    }
  }
}
