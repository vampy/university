package model.statement;

import java.util.ArrayList;
import java.util.List;

public class DeclareProcStatement extends Statement
{
    private String       name;
    private List<String> variables;
    private Statement body;

    public DeclareProcStatement(String name, List<String> variables, Statement body)
    {
        this.name = name;
        this.variables = variables;
        this.body = body;
    }

    /**
     * Getter for property 'name'.
     *
     * @return Value for property 'name'.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Getter for property 'variables'.
     *
     * @return Value for property 'variables'.
     */
    public List<String> getVariables()
    {
        return variables;
    }

    /**
     * Getter for property 'body'.
     *
     * @return Value for property 'body'.
     */
    public Statement getBody()
    {
        return body;
    }

    @Override
    public String toString()
    {
        return String.format("declare procedure %s(%s) { %s }", name, variables.toString(), body.toString());
    }

    @Override
    public Statement cloneDeep()
    {
        List<String> clone = new ArrayList<String>(variables.size());
        for (String var: variables)
        {
            clone.add(var);
        }

        return new DeclareProcStatement(name, clone, body.cloneDeep());
    }
}
