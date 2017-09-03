package huxg.framework.security;

public enum RoleType {
	PlatformAdmin, ProjectAdmin, BJTeacher, KCTeacher,Student;

	public static RoleType getType(String name) {
		RoleType rt = RoleType.valueOf(name);
		return rt;
	}

	public String getId() {
		if (this == PlatformAdmin) {
			return "8a8a99183ad86489013ad86f98360010";
		} else if (this == ProjectAdmin) {
			return "ff8080814144c7d7014144ccd8d40001";
		} else if (this == BJTeacher) {
			return "ff8080814144c7d7014144ce73a90003";
		} else if (this == KCTeacher){
			return "ff808081417937c701417939aef60001";
		} else if (this == Student) {
			return "ff8080814144c7d7014144cd12120002";
		}
		return null;
	}
}
