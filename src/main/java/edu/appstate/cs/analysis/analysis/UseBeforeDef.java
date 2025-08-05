package edu.appstate.cs.analysis.analysis;

import edu.appstate.cs.analysis.ast.*;
import edu.appstate.cs.analysis.cfg.CFG;
import edu.appstate.cs.analysis.cfg.Node;
import edu.appstate.cs.analysis.visitor.AnalysisVisitor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class UseBeforeDef {
    private CFG cfg = null;
    private Map<String, Set<ReachingDefs.Def>> reachingDefs;

    public UseBeforeDef(CFG cfg, Map<String, Set<ReachingDefs.Def>> reachingDefs) {
        this.cfg = cfg;
        this.reachingDefs = reachingDefs;
    }

    public static class UseBeforeDefError {
        private String nodeId;
        private String varName;

        public UseBeforeDefError(String nodeId, String varName) {
            this.nodeId = nodeId;
            this.varName = varName;
        }

        @Override
        public String toString() {
            return "UseBeforeDefError{" +
                    "nodeId='" + nodeId + '\'' +
                    ", varName='" + varName + '\'' +
                    '}';
        }
    }

    public Set<UseBeforeDefError> computeUseBeforeDefErrors() {
        Set<UseBeforeDefError> useBeforeDefs = new HashSet<>();
        UseBeforeDefVisitor useBeforeDefVisitor = new UseBeforeDefVisitor();
        for (Node n : cfg.getNodes()) {
            Set<UseBeforeDefError> res = n.accept(useBeforeDefVisitor);
            if (res != null) {
                useBeforeDefs.addAll(res);
            }
        }
        return useBeforeDefs;
    }

    private class UseBeforeDefVisitor implements AnalysisVisitor<Set<UseBeforeDefError>> {

        @Override
        public Set<UseBeforeDefError> visitStmtList(StmtList stmtList) {
            return Set.of();
        }

        @Override
        public Set<UseBeforeDefError> visitElseIfList(ElseIfList elseIfList) {
            return Set.of();
        }

        @Override
        public Set<UseBeforeDefError> visitExprList(ExprList exprList) {
            return Set.of();
        }

        @Override
        public Set<UseBeforeDefError> visitElseIf(ElseIf elseIf) {
            return Set.of();
        }

        @Override
        public Set<UseBeforeDefError> visitIfStmt(IfStmt ifStmt) {
            return Set.of();
        }

        @Override
        public Set<UseBeforeDefError> visitAssignStmt(AssignStmt assignStmt) {
            return Set.of();
        }

        @Override
        public Set<UseBeforeDefError> visitIntLiteral(IntLiteral intLiteral) {
            return Set.of();
        }

        @Override
        public Set<UseBeforeDefError> visitBooleanLiteral(BooleanLiteral booleanLiteral) {
            return Set.of();
        }

        @Override
        public Set<UseBeforeDefError> visitPlusExpr(PlusExpr plusExpr) {
            return Set.of();
        }

        @Override
        public Set<UseBeforeDefError> visitSubExpr(SubExpr subExpr) {
            return Set.of();
        }

        @Override
        public Set<UseBeforeDefError> visitMultExpr(MultExpr multExpr) {
            return Set.of();
        }

        @Override
        public Set<UseBeforeDefError> visitDivExpr(DivExpr divExpr) {
            return Set.of();
        }

        @Override
        public Set<UseBeforeDefError> visitIdentExpr(IdentExpr identExpr) {
            // Get the definitions that reach this node
            String nodeId = cfg.getNodeId(new Node.ExprNode(identExpr));
            Set<ReachingDefs.Def> defs = reachingDefs.get(nodeId);

            // Filter this to just definitions for the current name
            Set<ReachingDefs.Def> defsForIdent = defs.stream()
                    .filter((def -> { return def.getName().equals(identExpr.getIdent()); }))
                    .collect(Collectors.toSet());

            // Filter this further to just defs that are just declarations
            // NOTE: We could have done this above in the prior filtering step, this is just
            // to make it clearer what we are doing.
            Set<ReachingDefs.Def> uninitializedDefs = defsForIdent.stream()
                    .filter( def -> { return def.isDeclaration(); })
                    .collect(Collectors.toSet());

            // If the set of just-declaration defs is not empty, we have found use before def errors
            Set<UseBeforeDefError> useBeforeDefs = uninitializedDefs.stream()
                    .map(def -> { return new UseBeforeDefError(nodeId, def.getName()); })
                    .collect(Collectors.toSet());

            return useBeforeDefs;
        }

        @Override
        public Set<UseBeforeDefError> visitForStmt(ForStmt forStmt) {
            return Set.of();
        }

        @Override
        public Set<UseBeforeDefError> visitWhileStmt(WhileStmt whileStmt) {
            return Set.of();
        }

        @Override
        public Set<UseBeforeDefError> visitExprStmt(ExprStmt exprStmt) {
            return Set.of();
        }

        @Override
        public Set<UseBeforeDefError> visitNotEqlExpr(NotEqlExpr notEqlExpr) {
            return Set.of();
        }

        @Override
        public Set<UseBeforeDefError> visitEqualExpr(EqualExpr equalExpr) {
            return Set.of();
        }

        @Override
        public Set<UseBeforeDefError> visitDeclStmt(DeclStmt declStmt) {
            return Set.of();
        }

        @Override
        public Set<UseBeforeDefError> visitNotExpr(NotExpr notExpr) {
            return Set.of();
        }

        @Override
        public Set<UseBeforeDefError> visitReturnStmt(ReturnStmt returnStmt) {
            return Set.of();
        }

        @Override
        public Set<UseBeforeDefError> visitOrExpr(OrExpr orExpr) {
            return Set.of();
        }

        @Override
        public Set<UseBeforeDefError> visitAndExpr(AndExpr andExpr) {
            return Set.of();
        }

        @Override
        public Set<UseBeforeDefError> visitLtExpr(LtExpr ltExpr) {
            return Set.of();
        }

        @Override
        public Set<UseBeforeDefError> visitLtEqExpr(LtEqExpr ltEqExpr) {
            return Set.of();
        }

        @Override
        public Set<UseBeforeDefError> visitGtExpr(GtExpr gtExpr) {
            return Set.of();
        }

        @Override
        public Set<UseBeforeDefError> visitGtEqExpr(GtEqExpr gtEqExpr) {
            return Set.of();
        }

        @Override
        public Set<UseBeforeDefError> visitListExpr(ListExpr listExpr) {
            return Set.of();
        }

        @Override
        public Set<UseBeforeDefError> visitNegExpr(NegExpr negExpr) {
            return Set.of();
        }
    }
}
