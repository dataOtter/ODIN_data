package dao;

import orderedcollection.*;

public class CouponCollection extends MJ_OC<OneCoupon> {
	private IMJ_OC<OneCoupon> _allCoupons;
	
	public CouponCollection() {
		_allCoupons = new MJ_OC_Factory<OneCoupon>().create();
	}
	
	/**
	 * @param cid - the coupon ID for which to get OneCoupon
	 * @return the OneCoupon associated with the given coupon ID
	 */
	public OneCoupon getCouponById(int cid) {
		for (OneCoupon c: _allCoupons) {
            if (c.getId() == cid){
                return c;
            }
        }
        return null;
	}
	
	/**
	 * @return an IMJ_OC<Integer> of all coupon IDs found in this CouponCollection
	 */
	public IMJ_OC<Integer> getAllCids() {
		IMJ_OC<Integer> cids = new MJ_OC_Factory<Integer>().create();
		for (OneCoupon c: _allCoupons) {
			cids.add(c.getId());
		}
		return cids;
	}

	@Override
	public IMJ_OC<OneCoupon> getDeepCopy() {
		return _allCoupons.getDeepCopy();
	}

	@Override
	public void prepend(OneCoupon arg0) {
		_allCoupons.prepend(arg0);
	}

	@Override
	public void printAll() {
		_allCoupons.printAll();
	}

	@Override
	public boolean add(OneCoupon e) {
		return _allCoupons.add(e);
	}

	@Override
	public void add(int index, OneCoupon element) {
		_allCoupons.add(index, element);
	}

	@Override
	public void clear() {
		_allCoupons.clear();
	}

	@Override
	public OneCoupon get(int index) {
		return _allCoupons.get(index);
	}

	@Override
	public OneCoupon remove(int index) {
		return _allCoupons.remove(index);
	}

	@Override
	public OneCoupon set(int index, OneCoupon element) {
		return _allCoupons.set(index, element);
	}

	@Override
	public int size() {
		return _allCoupons.size();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return _allCoupons.toArray(a);
	}
}
