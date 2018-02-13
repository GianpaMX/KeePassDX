/*
 * Copyright 2017 Brian Pellin, Jeremy Jamet / Kunzisoft.
 *     
 * This file is part of KeePass DX.
 *
 *  KeePass DX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  KeePass DX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with KeePass DX.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.keepassdroid.database;

import com.keepassdroid.utils.StrUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public abstract class PwGroup extends PwNode {

    // TODO Change dependency and make private
	public List<PwGroup> childGroups = new ArrayList<>();
	public List<PwEntry> childEntries = new ArrayList<>();
	public String name = "";
	public PwIconStandard icon;

	private List<PwNode> children = new ArrayList<>();

    public void initNewGroup(String nm, PwGroupId newId) {
        setId(newId);
        name = nm;
    }

    public void addChildGroup(PwGroup group) {
        this.childGroups.add(group);
    }

    public void addChildEntry(PwEntry entry) {
        this.childEntries.add(entry);
    }

    public void removeChildGroup(PwGroup group) {
        this.childGroups.remove(group);
    }

    public void removeChildEntry(PwEntry entry) {
        this.childEntries.remove(entry);
    }

    public int numbersOfChildGroups() {
        return childGroups.size();
    }

    public int numbersOfChildEntries() {
        return childEntries.size();
    }

    @Override
	public Type getType() {
		return Type.GROUP;
	}

    /**
     * @return List of direct children (one level below) as PwNode
     */
    public List<PwNode> getDirectChildren() {
        children.clear();
        children.addAll(childGroups);
        children.addAll(childEntries);
        return children;
    }

    /**
     * Number of direct elements in Node (one level below)
     * @return Size of child elements, default is 0
     */
    public int numberOfDirectChildren() {
        return childGroups.size() + childEntries.size();
    }

    public boolean isContainedIn(PwGroup container) {
        PwGroup cur = this;
        while (cur != null) {
            if (cur == container) {
                return true;
            }
            cur = cur.getParent();
        }
        return false;
    }

	public abstract PwGroupId getId();
	public abstract void setId(PwGroupId id);

    @Override
    public String getDisplayTitle() {
        return getName();
    }

    public abstract String getName();
	
	public abstract Date getLastMod();
	
	public PwIcon getIcon() {
		return icon;
	}
	
	public abstract void setLastAccessTime(Date date);

	public abstract void setLastModificationTime(Date date);
	
	public void touch(boolean modified, boolean touchParents) {
		Date now = new Date();
		setLastAccessTime(now);
		
		if (modified) {
			setLastModificationTime(now);
		}
		
		PwGroup parent = getParent();
		if (touchParents && parent != null) {
			parent.touch(modified, true);
		}
	}
	
	public void searchEntries(SearchParameters sp, List<PwEntry> listStorage) {
		if (sp == null)  { return; }
		if (listStorage == null) { return; }
		
		List<String> terms = StrUtil.splitSearchTerms(sp.searchString);
		if (terms.size() <= 1 || sp.regularExpression) {
			searchEntriesSingle(sp, listStorage);
			return;
		}
		
		// Search longest term first
		Comparator<String> stringLengthComparator = new Comparator<String>() {
	
			@Override
			public int compare(String lhs, String rhs) {
				return lhs.length() - rhs.length();
			}
			
		};
		Collections.sort(terms, stringLengthComparator);
		
		String fullSearch = sp.searchString;
		List<PwEntry> pg = this.childEntries;
		for (int i = 0; i < terms.size(); i ++) {
			List<PwEntry> pgNew = new ArrayList<PwEntry>();
			
			sp.searchString = terms.get(i);
			
			boolean negate = false;
			if (sp.searchString.startsWith("-")) {
				sp.searchString.substring(1);
				negate = sp.searchString.length() > 0;
			}
			
			if (!searchEntriesSingle(sp, pgNew)) {
				pg = null;
				break;
			}
			
			List<PwEntry> complement = new ArrayList<PwEntry>();
			if (negate) {
				for (PwEntry entry: pg) {
					if (!pgNew.contains(entry)) {
						complement.add(entry);
					}
				}
				pg = complement;
			}
			else {
				pg = pgNew;
			}
		}
		
		if (pg != null) {
			listStorage.addAll(pg);
		}
		sp.searchString = fullSearch;

	}
	
	private boolean searchEntriesSingle(SearchParameters spIn, List<PwEntry> listStorage) {
		SearchParameters sp = (SearchParameters) spIn.clone();
		
		EntryHandler<PwEntry> eh;
		if (sp.searchString.length() <= 0) {
			eh = new EntrySearchHandlerAll(sp, listStorage);
		} else {
			eh = EntrySearchHandler.getInstance(this, sp, listStorage);
		}
		
		if (!preOrderTraverseTree(null, eh)) { return false; }
		
		return true;
	}

	public boolean preOrderTraverseTree(GroupHandler<PwGroup> groupHandler, EntryHandler<PwEntry> entryHandler) {
		if (entryHandler != null) {
			for (PwEntry entry : childEntries) {
				if (!entryHandler.operate(entry)) return false;
			}
		}
	
		for (PwGroup group : childGroups) {
			if ((groupHandler != null) && !groupHandler.operate(group)) return false;
			group.preOrderTraverseTree(groupHandler, entryHandler);
		}
		
		return true;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PwGroup pwGroup = (PwGroup) o;
        return isSameType(pwGroup)
                && (getId() != null ? getId().equals(pwGroup.getId()) : pwGroup.getId() == null);
    }

    @Override
    public int hashCode() {
        PwGroupId groupId = getId();
        return groupId != null ? groupId.hashCode() : 0;
    }

    /**
     * Group comparator by name
     */
    public static class GroupNameComparator implements Comparator<PwGroup> {
        public int compare(PwGroup object1, PwGroup object2) {
            if (object1.equals(object2))
                return 0;

            int groupNameComp = object1.getName().compareToIgnoreCase(object2.getName());
            // If same name, can be different
            if (groupNameComp == 0) {
                return object1.hashCode() - object2.hashCode();
            }
            return groupNameComp;
        }
    }

    /**
     * Group comparator by name
     */
    public static class GroupCreationComparator implements Comparator<PwGroup> {
        public int compare(PwGroup object1, PwGroup object2) {
            if (object1.equals(object2))
                return 0;

            int groupCreationComp = object1.getCreationTime().compareTo(object2.getCreationTime());
            // If same creation, can be different
            if (groupCreationComp == 0) {
                return object1.hashCode() - object2.hashCode();
            }
            return groupCreationComp;
        }
    }
}
