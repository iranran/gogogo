/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2010-12-10
 * 
 * Copyright (c) 2010 北京新媒传信科技有限公司
 */
package hello.programmer.common.basic;

/**
 * 
 * <b>描述: </b>简单的包含两个参数的泛型模板类，不能用于序列化
 * <p>
 * <b>功能: </b>二元组
 * <p>
 * <b>用法: </b>二元组的正常用法
 * <p>
 *
 *
 * @param <T1>
 * @param <T2>
 */
public class Combo2<T1, T2>
{
	private T1 v1;
	private T2 v2;

	public Combo2(T1 v1, T2 v2) {
		this.v1 = v1;
		this.v2 = v2;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 * @return
	 */
	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + ((v1 == null) ? 0 : v1.hashCode());
		result = prime * result + ((v2 == null) ? 0 : v2.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj){
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Combo2<?, ?> other = (Combo2<?, ?>) obj;
		if (v1 == null) {
			if (other.v1 != null)
				return false;
		} else if (!v1.equals(other.v1))
			return false;
		if (v2 == null) {
			if (other.v2 != null)
				return false;
		} else if (!v2.equals(other.v2))
			return false;
		return true;
	}



	public T1 getV1() {
		return v1;
	}

	public void setV1(T1 v1) {
		this.v1 = v1;
	}

	public T2 getV2() {
		return v2;
	}

	public void setV2(T2 v2) {
		this.v2 = v2;
	}
}
