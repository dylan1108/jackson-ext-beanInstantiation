package com.jackson.ext.test.vo;

public class MockPojoWithNonStaticInnerPojo_Outer {
    private String outerName;
    private MockPojoWithNonStaticInnerPojo_Inner mockPojoWithNonStaticInnerPojo_inner;

    public MockPojoWithNonStaticInnerPojo_Inner getMockPojoWithNonStaticInnerPojo_inner() {
        return mockPojoWithNonStaticInnerPojo_inner;
    }

    public void setMockPojoWithNonStaticInnerPojo_inner(MockPojoWithNonStaticInnerPojo_Inner mockPojoWithNonStaticInnerPojo_inner) {
        this.mockPojoWithNonStaticInnerPojo_inner = mockPojoWithNonStaticInnerPojo_inner;
    }

    public MockPojoWithNonStaticInnerPojo_Outer(String outerName) {
        this.outerName = outerName;
    }

    public String getOuterName() {
        return outerName;
    }

    public void setOuterName(String outerName) {
        this.outerName = outerName;
    }

    @Override
    public String toString() {
        return "MockPojoWithNonStaticInnerPojo_Outer{" +
                "outerName='" + outerName + '\'' +
                ", mockPojoWithNonStaticInnerPojo_inner=" + mockPojoWithNonStaticInnerPojo_inner +
                '}';
    }

    public class MockPojoWithNonStaticInnerPojo_Inner{
        private String innerName;

        public String getInnerName() {
            return innerName;
        }

        public void setInnerName(String innerName) {
            this.innerName = innerName;
        }

        public MockPojoWithNonStaticInnerPojo_Inner(String innerName) {
            this.innerName = innerName;
        }

        @Override
        public String toString() {
            return "MockPojoWithNonStaticInnerPojo_Inner{" +
                    "innerName='" + innerName + '\'' +
                    '}';
        }
    }
}
