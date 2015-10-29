package model;

import model.statement.DeclareProcStatement;

import java.util.HashMap;

public class ProcTable
{
    // Map from name => Declaration
    private HashMap<String, DeclareProcStatement> procedures = new HashMap<String, DeclareProcStatement>();

    public void add(DeclareProcStatement procedure) // TODO check if exists
    {
        procedures.put(procedure.getName(), procedure);
    }

    public DeclareProcStatement get(String name) // TODO check if exists
    {
        return procedures.get(name);
    }

    @Override
    public String toString()
    {
        return procedures.toString();
    }
}
