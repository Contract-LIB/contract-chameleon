package org.contract_lib.contract_chameleon.cl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;

import org.contract_lib.contract_chameleon.Adapter;
import org.contract_lib.contract_chameleon.AdapterArgument;
import org.contract_lib.contract_chameleon.AdapterArgumentProvider;
import org.contract_lib.contract_chameleon.AdapterMap;
import org.contract_lib.contract_chameleon.error.ChameleonMessageManager;
import org.contract_lib.contract_chameleon.error.SimpleErrorMessage;

public final class ArgumentParser implements AdapterArgumentProvider {

  private final Map<String, AdapterArgument> parseMap = new HashMap<>();
  private final Map<Class<AdapterArgument>, AdapterArgument> parsedArguments = new HashMap<>();

  private final AdapterMap<Adapter> adapterMap;

  private Optional<Adapter> providedAdapter = Optional.empty();
  private Optional<String> inputPath = Optional.empty();

  public ArgumentParser() {
    ServiceLoader.load(AdapterArgument.class).stream()
        .map(Provider::get)
        .forEach(this::addToMap);

    adapterMap = new AdapterMap<Adapter>(Adapter.class);
  }

  public Optional<Adapter> getAdapter() {
    return providedAdapter;
  }

  public Optional<String> getInputPath() {
    return inputPath;
  }

  @Override
  public <A extends AdapterArgument> Optional<A> getAdapterArgument(Class<A> adapterClass) {
    return Optional.ofNullable((A) parsedArguments.get(adapterClass));
  }

  public Map<Class<AdapterArgument>, AdapterArgument> getArguments() {
    return new HashMap<>(parsedArguments);
  }

  public void parseArguments(
      String[] args,
      ChameleonMessageManager messageManager) {
    List<String> argList = new ArrayList<>(List.of(args));

    if (argList.isEmpty()) {
      messageManager.report(new SimpleErrorMessage("No adapter selected."));
      return;
    }
    String adapterIdentifier = argList.removeFirst();
    providedAdapter = Optional.ofNullable(adapterMap.get(adapterIdentifier));

    //TODO: Fix for adapters that take no input path (help, list)
    if (argList.isEmpty()) {
      messageManager.report(new SimpleErrorMessage("No input file or path provided."));
      return;
    }
    inputPath = Optional.of(argList.removeFirst());

    Optional<ArgumentPair> activeArgument = Optional.empty();
    List<String> values = new ArrayList<>();

    for (String s : argList) {
      AdapterArgument matchedArgument = parseMap.get(s);
      // Add value to list if argument is defined
      if (matchedArgument == null) {
        if (activeArgument.isPresent()) {
          values.add(s);
        } else {
          messageManager.report(new SimpleErrorMessage(String.format("Value provided but argument expected: %s", s)));
        }
      } else {
        // Close argumnet list, parse values for the argument, and set new argument
        if (parsedArguments.containsKey(matchedArgument.getClass())) {
          messageManager.report(new SimpleErrorMessage("Argument was already defined before."));
          continue;
        }
        activeArgument.ifPresent(arg -> {
          parsedArguments.put((Class<AdapterArgument>) arg.arg().getClass(), arg.arg());
        });
        values.clear();
        activeArgument = Optional.of(new ArgumentPair(matchedArgument, s));
      }
    }
    activeArgument.ifPresent(arg -> {
      arg.arg().parseArguments(arg.label(), messageManager, values);
      parsedArguments.put((Class<AdapterArgument>) arg.arg().getClass(), arg.arg());
    });
  }

  void addToMap(AdapterArgument argument) {
    argument.getLabels()
        .stream()
        .forEach(a -> parseMap.put(a, argument));
  }

  private final record ArgumentPair(AdapterArgument arg, String label) {
  }
}
