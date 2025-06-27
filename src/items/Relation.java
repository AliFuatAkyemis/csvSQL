package items;

        interface Relation<T> {
        public void addColumn(String columnName, Class<T> type);
        public void dropColumn(String columnName, Class<T> type);
        public void insert(Object[] values);
        public void delete(String columnName, Object value);
}
