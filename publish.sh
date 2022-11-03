echo "PUBLISHING to $SNOWSQL_WORK_DATABASE"

~/bin/snowsql -o quiet=true <<QUERY
use database $SNOWSQL_WORK_DATABASE;

create stage if not exists workstage;
put file://./target/scala-2.12/ci-cd-sample-assembly-1.0-$GITHUB_REF_NAME.jar @workstage overwrite=true  auto_compress=FALSE;


create or replace stage public.pdf_external
url="s3://sfquickstarts/Analyze PDF Invoices/Invoices/"
directory = (enable = TRUE);

create or replace function public.extract_pdf_text(file string)
returns String
language java
imports = ('@workstage/ci-cd-sample-assembly-1.0-$GITHUB_REF_NAME.jar')
HANDLER = 'transformer.PDFUtils.ReadFile';
QUERY