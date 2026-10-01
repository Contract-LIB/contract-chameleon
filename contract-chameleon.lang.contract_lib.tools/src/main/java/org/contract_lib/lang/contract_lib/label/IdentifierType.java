package org.contract_lib.lang.contract_lib.label;

public interface IdentifierType {

  public String getMessageDescription();

  public final class SortIdentifier implements IdentifierType {
    @Override
    public String getMessageDescription() {
      return "sort";
    }
  }

  public final class FunctionIdentifier implements IdentifierType {
    @Override
    public String getMessageDescription() {
      return "function";
    }
  }

  public final class VariableIdentifier implements IdentifierType {
    @Override
    public String getMessageDescription() {
      return "variable";
    }
  }
}
