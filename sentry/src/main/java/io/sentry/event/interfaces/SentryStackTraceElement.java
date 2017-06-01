package io.sentry.event.interfaces;

import io.sentry.jvmti.Frame;
import io.sentry.jvmti.LocalsCache;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Richer StackTraceElement class.
 */
public class SentryStackTraceElement {
    private final String module;
    private final String function;
    private final String fileName;
    private final int lineno;
    private final Integer colno;
    private final String absPath;
    private final String platform;
    private final Map<String, Object> vars;

    /**
     * Construct a SentryStackTraceElement.
     *
     * @param module Module (class) name.
     * @param function Function (method) name.
     * @param fileName Filename.
     * @param lineno Line number.
     * @param colno Column number.
     * @param absPath Absolute path.
     * @param platform Platform name.
     * @param vars Local variables.
     */
    // CHECKSTYLE.OFF: ParameterNumber
    public SentryStackTraceElement(String module, String function, String fileName, int lineno,
                                   Integer colno, String absPath, String platform, Map<String, Object> vars) {
        this.module = module;
        this.function = function;
        this.fileName = fileName;
        this.lineno = lineno;
        this.colno = colno;
        this.absPath = absPath;
        this.platform = platform;
        this.vars = vars;
    }
    // CHECKSTYLE.ON: ParameterNumber

    public String getModule() {
        return module;
    }

    public String getFunction() {
        return function;
    }

    public String getFileName() {
        return fileName;
    }

    public int getLineno() {
        return lineno;
    }

    public Integer getColno() {
        return colno;
    }

    public String getAbsPath() {
        return absPath;
    }

    public String getPlatform() {
        return platform;
    }

    public Map<String, Object> getVars() {
        return vars;
    }

    /**
     * Convert an array of {@link StackTraceElement}s to {@link SentryStackTraceElement}s.
     *
     * @param stackTraceElements Array of {@link StackTraceElement}s to convert.
     * @return Array of {@link SentryStackTraceElement}s.
     */
    public static SentryStackTraceElement[] fromStackTraceElements(StackTraceElement[] stackTraceElements) {
        Frame[] localsCache = LocalsCache.getCache();
        SentryStackTraceElement[] sentryStackTraceElements = new SentryStackTraceElement[stackTraceElements.length];
        boolean mayHaveLocals = localsCache.length == stackTraceElements.length;

        for (int i = 0; i < stackTraceElements.length; i++) {
            Map<String, Object> vars = null;
            if (mayHaveLocals) {
                Frame frame = localsCache[i];
                if (!frame.getNamedLocals().isEmpty()) {
                    vars = new HashMap<>();
                    for (Map.Entry<String, Frame.LocalVariable> variableEntry : frame.getNamedLocals().entrySet()) {
                        vars.put(variableEntry.getKey(), variableEntry.getValue().getValue());
                    }
                }
            }
            sentryStackTraceElements[i] = fromStackTraceElement(stackTraceElements[i], vars);
        }
        return sentryStackTraceElements;
    }

    /**
     * Convert a single {@link StackTraceElement} to a {@link SentryStackTraceElement}.
     *
     * @param stackTraceElement {@link StackTraceElement} to convert.
     * @return {@link SentryStackTraceElement}
     */
    public static SentryStackTraceElement fromStackTraceElement(StackTraceElement stackTraceElement) {
        return fromStackTraceElement(stackTraceElement, null);
    }

    private static SentryStackTraceElement fromStackTraceElement(StackTraceElement stackTraceElement,
                                                                 Map<String, Object> vars) {
        return new SentryStackTraceElement(
            stackTraceElement.getClassName(),
            stackTraceElement.getMethodName(),
            stackTraceElement.getFileName(),
            stackTraceElement.getLineNumber(),
            null,
            null,
            null,
            vars
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SentryStackTraceElement that = (SentryStackTraceElement) o;
        return lineno == that.lineno
            && Objects.equals(module, that.module)
            && Objects.equals(function, that.function)
            && Objects.equals(fileName, that.fileName)
            && Objects.equals(colno, that.colno)
            && Objects.equals(absPath, that.absPath)
            && Objects.equals(platform, that.platform)
            && Objects.equals(vars, that.vars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(module, function, fileName, lineno, colno, absPath, platform, vars);
    }

    @Override
    public String toString() {
        return "SentryStackTraceElement{"
            + "module='" + module + '\''
            + ", function='" + function + '\''
            + ", fileName='" + fileName + '\''
            + ", lineno=" + lineno
            + ", colno=" + colno
            + ", absPath='" + absPath + '\''
            + ", platform='" + platform + '\''
            + ", vars='" + vars + '\''
            + '}';
    }
}
