package model;

import exceptions.StackException;
import model.statement.Statement;

/**
 * The interface I stack.
 */
public interface IStack extends java.io.Serializable
{
    /**
     * Top statement.
     *
     * @return the statement
     * @throws StackException the stack exception
     */
    public Statement top() throws StackException;

    /**
     * Pop statement.
     *
     * @return the statement
     * @throws StackException the stack exception
     */
    public Statement pop() throws StackException;

    /**
     * Getter for saving the stack into a file
     *
     * @return String
     */
    public String getAllSave();

    /**
     * Push void.
     *
     * @param element the element
     * @throws StackException the stack exception
     */
    public void push(Statement element) throws StackException;

    public void clear();

    /**
     * Is empty.
     *
     * @return the boolean
     */
    public boolean isEmpty();
}