package hmoa.hmoaserver.common;

public enum PageSize {
    ZERO_PAGE(0),
    FIVE_SIZE(5),
    SIX_SIZE(6),
    TEN_SIZE(10),
    FIFTY_SIZE(15),
    DEFAULT_CURSOR(9_999_999);

    private final int size;

    PageSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return this.size;
    }
}
