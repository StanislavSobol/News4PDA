package com.gmal.sobol.i.stanislav.news4pda;

public class CallbackBundle {

    public Runnable getResult() {
        return result;
    }

    public void setResult(Runnable result) {
        this.result = result;
    }

    public Runnable getError() {
        return error;
    }

    public void setError(Runnable error) {
        this.error = error;
    }

    private Runnable result;
    private Runnable error;
}
