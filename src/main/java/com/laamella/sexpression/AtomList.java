package com.laamella.sexpression;

import java.util.LinkedList;
import java.util.List;

public class AtomList extends SExpressionNode {
    public final List<SExpressionNode> values = new LinkedList<>();

    public void add(SExpressionNode sExpressionNode) {
        values.add(sExpressionNode);
    }

    public void add(CharSequence atom) {
        values.add(new Atom(atom));
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("(");
        String ws="";
        for(SExpressionNode node: values){
            s.append(ws).append(node.toString());
            ws=" ";
        }
        return s.append(")").toString();
    }
} 
