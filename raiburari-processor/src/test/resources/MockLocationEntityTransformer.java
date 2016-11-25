package mock;

import com.addhen.android.raiburari.processor.mock.MockLocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Transforms a {@link MockLocationEntity} into an {@link MockLocation}
 */
class MockLocationEntityTransformer {

    public MockLocationEntityTransformer() {
    }

    public MockLocation transform(MockLocationEntity mockLocationEntity) {
        if (mockLocationEntity == null) {
            throw new IllegalArgumentException("mockLocationEntity is null!");
        }
        MockLocation mocklocation = new MockLocation();
        mocklocation.locationName = mockLocationEntity.locationName;
        return mocklocation;
    }

    public List<MockLocation> transform(Collection<MockLocationEntity> mocklocationCollection) {
        List<MockLocation> mocklocationList = new ArrayList<>();
        for (MockLocationEntity mockLocationEntity : mocklocationCollection) {
            MockLocation mocklocation = transform(mockLocationEntity);
            if (mocklocation != null) {
                mocklocationList.add(mocklocation);
            }
        }
        return mocklocationList;
    }
}