package mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Transforms a {@link MockUserEntity} into an {@link MockUserEntity}
 */
class MockUserEntityTransformer {

    public MockUserEntityTransformer() {
    }

    public MockUserEntity transform(MockUserEntity mockUserEntity) {
        if (mockUserEntity == null) {
            throw new IllegalArgumentException("mockUserEntity is null!");
        }
        MockUserEntity mockuserentity = new MockUserEntity();
        mockuserentity.fullName = mockUserEntity.fullName;
        return mockuserentity;
    }

    public List<MockUserEntity> transform(Collection<MockUserEntity> mockuserentityCollection) {
        List<MockUserEntity> mockuserentityList = new ArrayList<>();
        for (MockUserEntity mockUserEntity : mockuserentityCollection) {
            MockUserEntity mockuserentity = transform(mockUserEntity);
            if (mockuserentity != null) {
                mockuserentityList.add(mockuserentity);
            }
        }
        return mockuserentityList;
    }
}