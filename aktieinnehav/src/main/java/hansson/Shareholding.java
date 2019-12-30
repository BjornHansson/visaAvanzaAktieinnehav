package hansson;

public class Shareholding {
    public static class Builder {
        private final String name;
        private double amount;

        public Builder(final String name) {
            this.name = name;
        }

        public Builder withAmount(final double amount) {
            this.amount = amount;
            return this;
        }

        public Shareholding build() {
            final Shareholding share = new Shareholding();
            share.name = this.name;
            share.amount = this.amount;
            return share;
        }
    }

    private String name;
    private double amount;

    private Shareholding() {
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(final double amount) {
        this.amount = amount;
    }

    public String getLabel() {
        return this.name + " " + this.amount + "%";
    }
}