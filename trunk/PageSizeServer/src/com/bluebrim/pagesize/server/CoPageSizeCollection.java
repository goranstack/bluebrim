package com.bluebrim.pagesize.server;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bluebrim.base.shared.CoSimpleObject;
import com.bluebrim.collection.shared.CoCollections;
import com.bluebrim.page.shared.CoPageSizeCollectionIF;
import com.bluebrim.page.shared.CoPageSizeIF;
import com.bluebrim.system.shared.CoGOI;

/**
 * Insert the type's description here.
 * Creation date: (2001-05-10 12:03:12)
 * @author: Dennis
 */

public class CoPageSizeCollection extends CoSimpleObject implements CoPageSizeCollectionIF {
	private List m_pageSizes = new ArrayList();
	private boolean m_dirty;

	public CoPageSizeCollection() {
		super();
	}

	public CoPageSizeIF addPageSize(CoPageSizeIF aPageSize) {
		m_pageSizes.add(aPageSize);
		markDirty();
		return aPageSize;
	}

	public void clear() {
		m_pageSizes.clear();
		markDirty();
	}

	public CoPageSizeIF createPageSize() {
		return new CoPageSize();
	}

	public CoPageSizeIF getPageSizeByName(final String name) {
		return (CoPageSizeIF) CoCollections.detect(m_pageSizes, new CoCollections.Detector() {
			public boolean detect(Object pageSize) {
				return ((CoPageSizeIF) pageSize).getName().equals(name);
			}
		});
	}
	
	public CoPageSizeIF lookupPageSize(CoGOI goi) {
		Iterator iter = m_pageSizes.iterator();
		while (iter.hasNext()) {
			CoPageSizeIF pageSize = (CoPageSizeIF) iter.next();
			if (pageSize.getGOI().equals(goi))
				return pageSize;
		}
		return null;
	}

	public List getPageSizes() {
		return m_pageSizes;
	}

	protected void markDirty() {
		m_dirty = !m_dirty;
	}

	public CoPageSizeIF removePageSize(CoPageSizeIF aPageSize) {
		m_pageSizes.remove(aPageSize);
		markDirty();
		return aPageSize;
	}
	
	public CoPageSizeCollectionIF copy() {
		CoPageSizeCollection psc = new CoPageSizeCollection();
		Iterator iter = getPageSizes().iterator();
		while (iter.hasNext()) {
			psc.addPageSize((CoPageSizeIF)iter.next());
		}
		return psc;
	}
	
	public void createDefaultPageSizes() {
		addPageSize(new CoPageSize( CoPageSizeIF.A4, CoPageSizeIF.A4_SIZE ) );
		addPageSize(new CoPageSize( CoPageSizeIF.A3, CoPageSizeIF.A3_SIZE ) );
		addPageSize(new CoPageSize( CoPageSizeIF.BROADSHEET, CoPageSizeIF.BROADSHEET_SIZE ) );
		addPageSize(new CoPageSize( CoPageSizeIF.TABLOID, CoPageSizeIF.TABLOID_SIZE ) );
	
	}
}
