package mock;

import com.addhen.android.raiburari.processor.mock.MockUser;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Transforms a {@link MockUserEntity} into an {@link MockUser}
 */
class MockUserEntityTransformer {

  public MockUserEntityTransformer() {
  }

  public MockUser transform(MockUserEntity mockUserEntity) {
    if (mockUserEntity == null) {
      throw new IllegalArgumentException("mockUserEntity is null!");
    }
    MockUser mockuser = new MockUser();
    mockuser.fullName = mockUserEntity.fullName;
    return mockuser;
  }

  public List<MockUser> transform(Collection<MockUserEntity> mockuserCollection) {
    List<MockUser> mockuserList = new ArrayList<>();
    for (MockUserEntity mockUserEntity : mockuserCollection) {
      MockUser mockuser = transform(mockUserEntity);
      if (mockuser != null) {
        mockuserList.add(mockuser);
      }
    }
    return mockuserList;
  }
}