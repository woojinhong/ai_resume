# ai_resume
이력서 자동화 분석 시스템


## 개요

### **How:**

이 시스템은 대량의 이력서를 자동으로 분석하고, **신입/경력** 여부를 기반으로 하위 카테고리로 분류합니다. 분석된 이력서는 **직무 적합성**에 따라 순위를 매겨, 면접관이 가장 적합한 지원자를 빠르게 선별할 수 있도록 도와줍니다. **GPT API**, **Redis 캐싱**, **Elasticsearch** 등을 활용해 이력서 분석 및 검색 속도를 최적화하며, **데이터베이스 인덱싱**을 통해 대량의 이력서를 효율적으로 처리합니다.

### **What:**

**이력서 분석 및 직무 매칭 시스템**은 면접관의 시간을 최소화하기 위해 개발되었습니다. 기업에서 제출된 수많은 이력서를 자동으로 분석하여 **신입/경력**을 구분하고, **기술 스택**, **경력 연수**, **학력** 등 하위 카테고리별로 지원자를 분류합니다. 그 후 각 지원자의 **직무 적합성**을 평가하여 순위를 매기고, 면접관이 우선적으로 검토해야 할 이력서를 제공합니다. 이를 통해 면접관이 효율적으로 채용 과정을 진행할 수 있도록 지원합니다.

### **Why:**

기업들은 채용 과정에서 수많은 이력서를 검토해야 하지만, 시간과 자원이 제한되어 있습니다. 이 시스템은 **면접관의 시간을 절약**하고, **직무에 적합한 지원자를 빠르게 선발**할 수 있도록 설계되었습니다. 이력서 분석과 순위 매김을 자동화함으로써, 채용 과정의 **효율성**을 높이고, 면접관들이 핵심 인재에게 집중할 수 있게 도와줍니다.

## 데이터베이스

### 1. **Applicants (지원자 테이블)**

- 각 PDF 이력서에 포함된 **지원자 정보**를 저장합니다. 이 정보는 이력서에서 자동으로 추출될 수 있으며, 주요 필드만 저장합니다.

| 필드 이름 | 타입 | 설명 |
| --- | --- | --- |
| `id` | BIGINT | 지원자 고유 식별자 (Primary Key) |
| `name` | VARCHAR(100) | 지원자 이름 |
| `email` | VARCHAR(150) | 지원자 이메일 |
| `phone_number` | VARCHAR(15) | 지원자 전화번호 |
| `created_at` | TIMESTAMP | 지원자 정보 등록일 |
| `updated_at` | TIMESTAMP | 지원자 정보 최신화 일자 |

### 2. **Resumes (이력서 테이블)**

- 면접관이 첨부한 이력서 파일에 대한 정보를 저장합니다. 각 PDF 이력서는 해시를 사용해 **중복 여부를 확인**할 수 있으며, **이력서 분석 결과**도 저장합니다.

| 필드 이름 | 타입 | 설명 |
| --- | --- | --- |
| `id` | BIGINT | 이력서 고유 식별자 (Primary Key) |
| `applicant_id` | BIGINT | 지원자 ID (Applicants 테이블의 Foreign Key) |
| `resume_hash` | VARCHAR(255) | 이력서 파일 해시 값 (중복 방지를 위한 고유 식별자) |
| `skills` | JSON | 기술 스택 (JSON 형태로 저장 가능) |
| `experience_years` | INT | 경력 연수 |
| `education` | VARCHAR(255) | 학력 정보 |
| `analysis_result` | JSON | GPT API를 통한 분석 결과 요약 |
| `created_at` | TIMESTAMP | 이력서 등록일 |
| `updated_at` | TIMESTAMP | 최근 업데이트 날짜 |

### 3. **Interviewers (면접관 테이블)**

- 면접관(인사 담당자)의 정보를 저장합니다.

| 필드 이름 | 타입 | 설명 |
| --- | --- | --- |
| `id` | BIGINT | 면접관 고유 식별자 (Primary Key) |
| `name` | VARCHAR(100) | 면접관 이름 |
| `email` | VARCHAR(150) | 면접관 이메일 |
| `created_at` | TIMESTAMP | 면접관 등록일 |
| `updated_at` | TIMESTAMP | 면접관 정보 최신화 일자 |

### 4. **ResumeUploads (이력서 업로드 테이블)**

- 면접관이 특정 이력서를 업로드한 내역을 기록합니다. 이는 **이력서와 면접관 간의 관계**를 저장하며, 어느 면접관이 어느 지원자의 이력서를 업로드했는지 추적할 수 있습니다.

| 필드 이름 | 타입 | 설명 |
| --- | --- | --- |
| `id` | BIGINT | 업로드 기록 고유 식별자 (Primary Key) |
| `interviewer_id` | BIGINT | 면접관 ID (Interviewers 테이블의 Foreign Key) |
| `resume_id` | BIGINT | 이력서 ID (Resumes 테이블의 Foreign Key) |
| `uploaded_at` | TIMESTAMP | 이력서 업로드 일자 |

### 5. **ResumeSections (이력서 섹션 테이블)**

- 이력서의 세부 섹션(예: 자기소개서, 기술 스택, 경력 등)을 나누어 저장하여 **변경된 부분만 재분석**할 수 있도록 처리합니다. 섹션별 해시 값으로 변경 사항을 관리합니다.

| 필드 이름 | 타입 | 설명 |
| --- | --- | --- |
| `id` | BIGINT | 섹션 고유 식별자 (Primary Key) |
| `resume_id` | BIGINT | 이력서 ID (Resumes 테이블의 Foreign Key) |
| `section_type` | VARCHAR(50) | 섹션 유형 (예: 자기소개서, 기술 스택, 경력 등) |
| `section_content` | TEXT | 섹션 내용 |
| `section_hash` | VARCHAR(255) | 섹션별 해시 값 (변경된 부분만 식별 및 캐싱) |
| `updated_at` | TIMESTAMP | 섹션 내용 업데이트 날짜 |

### 6. **ResumeViews (이력서 조회 기록 테이블)**

- 면접관이 업로드된 이력서를 조회한 기록을 남깁니다. 이를 통해 **어떤 면접관이 어떤 이력서를 언제 확인했는지** 추적할 수 있습니다.

| 필드 이름 | 타입 | 설명 |
| --- | --- | --- |
| `id` | BIGINT | 조회 기록 고유 식별자 (Primary Key) |
| `interviewer_id` | BIGINT | 면접관 ID (Interviewers 테이블의 Foreign Key) |
| `resume_id` | BIGINT | 이력서 ID (Resumes 테이블의 Foreign Key) |
| `viewed_at` | TIMESTAMP | 이력서 조회 일자 |

### 요약:

- **Applicants**: PDF에서 추출된 지원자 정보 저장.
- **Resumes**: 각 이력서의 해시 값과 분석 결과를 저장하여 중복을 피하고 빠르게 분석.
- **Interviewers**: 면접관(인사 담당자) 정보 저장.
- **ResumeUploads**: 면접관이 업로드한 이력서 내역을 추적.
- **ResumeSections**: 이력서의 중요한 섹션(자기소개서, 기술 스택 등)을 나눠 관리하여 부분적인 재분석 가능.
- **ResumeViews**: 면접관이 어떤 이력서를 조회했는지 기록.
