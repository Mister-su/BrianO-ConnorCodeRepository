package com.dinfo.oec.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class MultiBTree  implements Serializable{
	
	private String  cate_id;
	
	private String parent_cate_id;
	
	private int level;
	
	private List<MultiBTree> childs =null;

	
	
	public MultiBTree() {
		initChildList();
	}
	
	
	public MultiBTree(String cateId, String parentCateId) {
		super();
		cate_id = cateId;
		parent_cate_id = parentCateId;
	}

	public int getLevel() {
		return level;
	}


	public void setLevel(int level) {
		this.level = level;
	}


	public String getCate_id() {
		return cate_id;
	}

	public void setCate_id(String cateId) {
		cate_id = cateId;
	}

	public String getParent_cate_id() {
		return parent_cate_id;
	}

	public void setParent_cate_id(String parentCateId) {
		parent_cate_id = parentCateId;
	}

	public List<MultiBTree> getChilds() {
		return childs;
	}

	public void setChilds(List<MultiBTree> childs) {
		this.childs = childs;
	}
	
	public boolean  addTreeNode(MultiBTree treenode)
	{
		String pcate_id =treenode.getParent_cate_id();
		if(this.getCate_id().equals(pcate_id))
		{
			addChildNode(treenode);
			return true;
		}else{
			boolean addflag=false;
			if(!this.isLeaf())
			{
				for(MultiBTree node :childs)
				{
					if(node.addTreeNode(treenode)){
						return true;
					}
				}
			}
			
		}
		return false;
	}
	
//	public boolean addTreeNodeByStr(String cate_id)
//	{
//		
//	}
	
	/**
	 * 通过节点查找
	 * @param cate_id
	 * @return
	 */
	public MultiBTree searchTreeNodeByCate_id(String cate_id)
	{
		if(this.getCate_id().equals(cate_id))
			return this;
		if(childs==null||childs.isEmpty())
			return null;
		for(MultiBTree node :childs)
		{	
			MultiBTree findnode = node.searchTreeNodeByCate_id(cate_id);
			if(findnode!=null)
			{
				return findnode;
			}
		}
		return null;
	}
	
	public void addChildNode(MultiBTree treenode)
	{
		initChildList();
		treenode.setLevel(this.getLevel()+1);
		childs.add(treenode);
	}
	
	public void initChildList(){
		if(childs==null)
			childs= new ArrayList<MultiBTree>();
	}
	
	public List<String> getChildStrByLevel(String pcate_id ,int depth)
	{
		MultiBTree tree = searchTreeNodeByCate_id(pcate_id);
		List<String> cates = new ArrayList<String>();
		List<MultiBTree> datas = new ArrayList<MultiBTree>();
		tree.TreeTraverse(datas);
		if(null!=datas&&!datas.isEmpty())
		{
			for(MultiBTree node :datas)
			{	
				if(node.getLevel()>tree.getLevel()&&node.getLevel()<=tree.getLevel()+depth)
					cates.add(node.getCate_id());
			}
		}
		return cates;
	}
	
	/**
	 * 根据
	 * @param parent_id
	 * @param level
	 * @return
	 */
	public void  TreeTraverse(List<MultiBTree> trees)
	{
		for(MultiBTree tree :this.childs)
		{
			if(!tree.isLeaf())
			{	
				trees.add(tree);
				tree.TreeTraverse(trees);
			}else{
				trees.add(tree);
			}
		}
		
	}
	
	
	/**
	 *  是否是叶子节点
	 * @return
	 */
	public boolean isLeaf()
	{
		if(childs==null||childs.isEmpty())
		{
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 通过一个字符串转换出一个节点
	 * @param node
	 * @return
	 */
	protected MultiBTree createTreeNodeByStr(String node)
	{
		MultiBTree tnode =new MultiBTree();
		tnode.setCate_id(node);
		
		
		return tnode;
	}


	@Override
	public String toString() {
		return "MultiBTree [cate_id=" + cate_id + ", childs=" + childs
				+ ", level=" + level + ", parent_cate_id=" + parent_cate_id
				+ "]";
	}
	
	
	
}

