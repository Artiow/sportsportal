create function tournament.tour_bundle_check()
  returns trigger as $tour_bundle_check$
begin
  if (not (new.tournament_id isnull)) and (not (new.parent_id isnull))
  then raise exception 'invalid foreign keys';
  end if;
  if (new.tournament_id isnull) and (new.parent_id isnull)
  then raise exception 'invalid foreign keys';
  end if;
  return new;
end;
$tour_bundle_check$
language plpgsql;

create trigger tour_bundle_check
  before insert or update
  on tournament.tour_bundle
  for each row execute procedure
  tournament.tour_bundle_check();