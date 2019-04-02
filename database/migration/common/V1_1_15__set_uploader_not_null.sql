delete
from common.picture
where uploader_id isnull;

alter table common.picture
  alter column uploader_id set not null;